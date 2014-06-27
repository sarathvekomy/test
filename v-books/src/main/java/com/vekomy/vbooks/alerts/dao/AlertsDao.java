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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vekomy.vbooks.alerts.command.AlertsCommand;
import com.vekomy.vbooks.alerts.command.AlertsResult;
import com.vekomy.vbooks.alerts.command.TrendingAlertResult;
import com.vekomy.vbooks.alerts.command.ViewAlertResult;
import com.vekomy.vbooks.hibernate.BaseDao;
import com.vekomy.vbooks.hibernate.model.VbAlertCategory;
import com.vekomy.vbooks.hibernate.model.VbAlertType;
import com.vekomy.vbooks.hibernate.model.VbAlertTypeMySales;
import com.vekomy.vbooks.hibernate.model.VbAlertTypeMySalesPage;
import com.vekomy.vbooks.hibernate.model.VbEmployee;
import com.vekomy.vbooks.hibernate.model.VbEmployeeDetail;
import com.vekomy.vbooks.hibernate.model.VbExcessCash;
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
import com.vekomy.vbooks.util.OrganizationUtils;

/**
 * @author swarupa
 * 
 */
public class AlertsDao extends BaseDao {

	/**
	 * Logger variable holds _logger
	 */
	private static final Logger _logger = LoggerFactory
			.getLogger(AlertsDao.class);

	/**
	 * This method is responsible for persisting the alert into
	 * {@link VbSystemAlertsHistory}.
	 * 
	 * @param alertType
	 *            - {@link String}
	 * @param userName
	 *            - {@link String}
	 * @param organization
	 *            - {@link VbOrganization}
	 */
	public void saveSystemAlertsHistory(String alertType, String userName,
			VbOrganization organization) {
		Session session = this.getSession();
		Transaction txn = session.beginTransaction();
		Date date = new Date();
		VbAlertCategory alertCategory = getAlertCategoryForSystemAlerts(session);
		if (alertCategory != null) {
			VbAlertType vbAlertType = getVbAlertType(session, alertType, alertCategory);
			if (vbAlertType != null) {
				List<VbSystemAlerts> systemAlert = getVbSystemAlerts(session, vbAlertType, organization);
				for (VbSystemAlerts vbSystemAlerts : systemAlert) {
					VbSystemAlertsHistory systemAlertsHistory = new VbSystemAlertsHistory();
					systemAlertsHistory.setCreatedOn(date);
					systemAlertsHistory.setCreatedBy(userName);
					systemAlertsHistory.setModifiedOn(date);
					systemAlertsHistory.setVbSystemAlerts(vbSystemAlerts);

					if (_logger.isDebugEnabled()) {
						_logger.debug("Persisting VbSystemAlertsHistory: {}", systemAlertsHistory);
					}
					session.save(systemAlertsHistory);
					txn.commit();
				}
			}
		}
		session.close();
	}

	/**
	 * This method is responsible for persisting
	 * {@link VbUserDefinedAlertsHistory} into DB.
	 * 
	 * @param alertType
	 *            - {@link String}
	 * @param userName
	 *            - {@link String}
	 * @param organization
	 *            - {@link VbOrganization}
	 * 
	 */
	public void saveUserDefinedAlertsHistory(String alertType,
			String alertMysales, String alertMysalesPage, String userName,
			VbOrganization organization) {
		Session session = this.getSession();
		Transaction txn = session.beginTransaction();
		Date date = new Date();
		VbAlertCategory vbAlertCategory = getAlertCategoryForUserDefinedAlerts(session);
		if (vbAlertCategory != null) {
			VbAlertType vbAlertType = getVbAlertType(session, alertType, vbAlertCategory);
			if (vbAlertType != null) {
				List<VbUserDefinedAlerts> vbUserDefinedAlertsList = getVbUserDefinedAlerts(session, vbAlertType, organization);
				for (VbUserDefinedAlerts vbUserDefinedAlerts : vbUserDefinedAlertsList) {
					VbUserDefinedAlertsHistory vbUserDefinedAlertsHistory = new VbUserDefinedAlertsHistory();
					vbUserDefinedAlertsHistory.setCreatedBy(userName);
					vbUserDefinedAlertsHistory.setCreatedOn(date);
					vbUserDefinedAlertsHistory.setModifiedOn(date);
					vbUserDefinedAlertsHistory.setVbUserDefinedAlerts(vbUserDefinedAlerts);

					if (_logger.isDebugEnabled()) {
						_logger.debug("Persisting VbUserDefinedAlertsHistory: {}", vbUserDefinedAlertsHistory);
					}
					session.save(vbUserDefinedAlertsHistory);
					txn.commit();
				}
			}
		}
		session.close();
	}

	/**
	 * This method is responsible to get the mailid's for {@link VbSystemAlerts}
	 * .
	 * 
	 * @param groupName
	 *            - {@link String}
	 * @param organization
	 *            - {@link VbOrganization}
	 * @return mailList - {@link List}
	 */
	@SuppressWarnings("unchecked")
	public List<AlertsResult> getEmailIDByAlertTypeForSystemAlerts(
			String alertType, VbOrganization organization) {
		Session session = this.getSession();
		VbAlertCategory alertCategory = getAlertCategoryForSystemAlerts(session);
		VbAlertType vbAlertType = getVbAlertType(session, alertType, alertCategory);
		VbNotifications vbNotifications = getVbNotifications(session, OrganizationUtils.ALERT_NOTIFICATION_TYPE_EMAIL);
		List<VbSystemAlerts> vbSystemAlertsList = getVbSystemAlerts(session, vbAlertType, organization);
		List<AlertsResult> alertsEmailResultList = new ArrayList<AlertsResult>();
		AlertsResult alertsResult = null;
		List<VbSystemAlertsNotifications> systemAlertsNotifications = null;
		for (VbSystemAlerts vbSystemAlerts : vbSystemAlertsList) {
			systemAlertsNotifications = session.createCriteria(VbSystemAlertsNotifications.class)
					.add(Restrictions.eq("vbNotifications", vbNotifications))
					.add(Restrictions.eq("vbSystemAlerts", vbSystemAlerts))
					.list();
			for (VbSystemAlertsNotifications vbSystemAlertsNotifications : systemAlertsNotifications) {
				alertsResult = new AlertsResult();
				alertsResult.setEmailId(vbSystemAlertsNotifications.getVbEmployee().getEmployeeEmail());
				if (vbSystemAlertsNotifications.getMailsCc() == Boolean.TRUE) {
					alertsResult.setMailRecipientType(OrganizationUtils.EMAIL_RECEIPIENT_TYPE_CC);
				} else if (vbSystemAlertsNotifications.getMailsBcc() == Boolean.TRUE) {
					alertsResult.setMailRecipientType(OrganizationUtils.EMAIL_RECEIPIENT_TYPE_BCC);
				} else if (vbSystemAlertsNotifications.getMailsTo() == Boolean.TRUE) {
					alertsResult.setMailRecipientType(OrganizationUtils.EMAIL_RECEIPIENT_TYPE_TO);
				}
				alertsEmailResultList.add(alertsResult);
			}
		}
		session.close();

		if (_logger.isDebugEnabled()) {
			_logger.debug("{} mailids have been configured for alertType: {}", alertsEmailResultList.size(), alertType);
		}
		return alertsEmailResultList;
	}

	/**
	 * This method is responsible to get the mailid's for
	 * {@link VbUserDefinedAlerts}.
	 * 
	 * @param groupName
	 *            - {@link String}
	 * @param organization
	 *            - {@link VbOrganization}
	 * @return mailList - {@link List}
	 */
	@SuppressWarnings("unchecked")
	public List<AlertsResult> getEmailIDByAlertTypeForUserDefinedAlerts(String alertType, VbOrganization organization) {
		Session session = this.getSession();
		VbAlertCategory alertCategory = getAlertCategoryForSystemAlerts(session);
		VbAlertType vbAlertType = getVbAlertType(session, alertType, alertCategory);
		VbNotifications vbNotifications = getVbNotifications(session, OrganizationUtils.ALERT_NOTIFICATION_TYPE_EMAIL);
		List<VbUserDefinedAlerts> vbUserDefinedAlertsList = getVbUserDefinedAlerts(session, vbAlertType, organization);
		List<VbUserDefinedAlertsNotifications> userDefinedAlertsNotifications = null;
		AlertsResult alertsResult = null;
		List<AlertsResult> alertsEmailResultList = new ArrayList<AlertsResult>();
		for (VbUserDefinedAlerts vbUserDefinedAlerts : vbUserDefinedAlertsList) {
			userDefinedAlertsNotifications = session.createCriteria(VbUserDefinedAlertsNotifications.class)
					.add(Restrictions.eq("vbNotifications", vbNotifications))
					.add(Restrictions.eq("vbUserDefinedAlerts", vbUserDefinedAlerts)).list();
			
			for (VbUserDefinedAlertsNotifications vbUserDefinedAlertsNotifications : userDefinedAlertsNotifications) {
				alertsResult = new AlertsResult();
				alertsResult.setEmailId(vbUserDefinedAlertsNotifications.getVbEmployee().getEmployeeEmail());
				if (vbUserDefinedAlertsNotifications.getMailsCc() == Boolean.TRUE) {
					alertsResult.setMailRecipientType(OrganizationUtils.EMAIL_RECEIPIENT_TYPE_CC);
				} else if (vbUserDefinedAlertsNotifications.getMailsBcc() == Boolean.TRUE) {
					alertsResult.setMailRecipientType(OrganizationUtils.EMAIL_RECEIPIENT_TYPE_BCC);
				} else if (vbUserDefinedAlertsNotifications.getMailsTo() == Boolean.TRUE) {
					alertsResult.setMailRecipientType(OrganizationUtils.EMAIL_RECEIPIENT_TYPE_TO);
				}

				alertsEmailResultList.add(alertsResult);
			}
		}
		session.close();

		if (_logger.isDebugEnabled()) {
			_logger.debug("{} mailids have been configured for alertType: {}", alertsEmailResultList.size(), alertType);
		}
		return alertsEmailResultList;
	}

	/**
	 * This method is responsible to get all the mobile nos based on alert type.
	 * 
	 * @param alertType
	 *            - {@link String}
	 * @param organization
	 *            - {@link VbOrganization}
	 * @return mobileNoList - {@link List}
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<String> getMobileNoByAlertTypeForSystemAlerts(String alertType,
			VbOrganization organization) {
		Session session = this.getSession();
		VbAlertCategory vbAlertCategory = getAlertCategoryForSystemAlerts(session);
		VbAlertType vbAlertType = getVbAlertType(session, alertType, vbAlertCategory);
		List<VbSystemAlerts> vbSystemAlertsList = getVbSystemAlerts(session, vbAlertType, organization);
		VbNotifications vbNotifications = getVbNotifications(session, OrganizationUtils.ALERT_NOTIFICATION_TYPE_SMS);
		List<String> mobileNoList = new ArrayList<String>();
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
		return mobileNoList;
	}

	/**
	 * This method is responsible to get the mail id associated with particular
	 * user.
	 * 
	 * @param userName
	 *            - {@link String}
	 * @param organization
	 *            - {@link VbOrganization}
	 * @return emailId - {@link String}
	 */
	public String getEmailIDByName(String userName, VbOrganization organization) {
		Session session = this.getSession();
		String emailId = (String) session.createCriteria(VbEmployee.class)
				.setProjection(Projections.property("employeeEmail"))
				.add(Restrictions.eq("username", userName))
				.add(Restrictions.eq("vbOrganization", organization))
				.uniqueResult();
		session.close();
		
		if (_logger.isDebugEnabled()) {
			_logger.debug("{} associated with user is {}", emailId, userName);
		}
		return emailId;
	}

	/**
	 * This method is used to get the alert type list.
	 * 
	 * @param organization
	 *            {@link VbOrganization}
	 * @return alertTypeList - {@link List}
	 */
	@SuppressWarnings("unchecked")
	public List<String> getAlertType() {
		Session session = this.getSession();
		List<String> alertTypeList = session.createCriteria(VbAlertType.class)
				.setProjection(Projections.property("alertType"))
				.add(Restrictions.eq("vbAlertCategory", getAlertCategoryForSystemAlerts(session)))
				.list();
		session.close();
		
		if (_logger.isDebugEnabled()) {
			_logger.debug("Configured alert types are {}", alertTypeList);
		}
		return alertTypeList;
	}

	/**
	 * This method is responsible to get the list of configured groups.
	 * 
	 * @param organization
	 *            - {@link VbOrganization}
	 * @return groupList - {@link List}
	 */
	@SuppressWarnings("unchecked")
	public List<String> getConfiguredGroups(VbOrganization organization) {
		Session session = this.getSession();
		List<String> groupList = session.createCriteria(VbRole.class)
				.setProjection(Projections.property("description"))
				.add(Restrictions.ne("description", "User"))
				.add(Restrictions.ne("description", "Site Administrator"))
				.add(Restrictions.ne("description", "Group Head")).list();
		session.close();
		
		if (_logger.isDebugEnabled()) {
			_logger.debug("Configured Groups are {}", groupList);
		}
		return groupList;
	}

	/**
	 * This method is responsible to get the associated users under particular
	 * group.
	 * 
	 * @param group
	 *            - {@link String}
	 * @param organization
	 *            - {@link VbOrganization}
	 * @return userList - {@link List}
	 */
	@SuppressWarnings("unchecked")
	public List<String> getAssociatedUsers(String group, VbOrganization organization) {
		Session session = this.getSession();
		String empType = OrganizationUtils.getEmployeeTypeByRole(group);
		List<String> userList = session.createCriteria(VbEmployee.class)
				.setProjection(Projections.property("username"))
				.add(Restrictions.eq("employeeType", empType))
				.add(Restrictions.eq("vbOrganization", organization))
				.list();
		session.close();
		
		if (_logger.isDebugEnabled()) {
			_logger.debug("Associated users under group {} are {}", group, userList);
		}
		return userList;
	}

	@SuppressWarnings("unchecked")
	public void saveSystemDefinedAlert(AlertsCommand alertsCommand,
			String notificationType, String group, String userNames,
			VbOrganization organization, String createdBy) {
		Session session = this.getSession();
		Transaction txn = session.beginTransaction();
		String groupDropdown = null;
		Date date = new Date();
		// Persisting VbSystemAlerts.
		VbAlertCategory alertCategory = getAlertCategoryForSystemAlerts(session);
		VbAlertType alertType = getVbAlertType(session,
				alertsCommand.getAlertType(), alertCategory);
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

		String[] notifications = notificationType.split(",");
		List<String> notificationList = Arrays.asList(notifications);
		VbNotifications vbNotifications = null;
		List<VbEmployee> vbEmployeeList = null;
		VbSystemAlertsNotifications systemAlertsNotifications = null;
		VbRole vbRole = null;
		String empType = null;
		for (String notification : notificationList) {
			vbNotifications = (VbNotifications) session
					.createCriteria(VbNotifications.class)
					.add(Restrictions.eq("notificationType", notification))
					.uniqueResult();
			if (group != "") {
				String[] groupReceipient = group.split("/");
				String groupName = groupReceipient[0];
				if (OrganizationUtils.ALERT_NOTIFICATION_TYPE_EMAIL.equalsIgnoreCase(notificationType)) {
					groupDropdown = groupReceipient[1];
				}

				vbRole = (VbRole) session.createCriteria(VbRole.class)
						.add(Restrictions.eq("description", groupName))
						.uniqueResult();
				empType = OrganizationUtils.getEmployeeTypeByRole(groupName);
				vbEmployeeList = session.createCriteria(VbEmployee.class)
						.add(Restrictions.eq("employeeType", empType))
						.add(Restrictions.eq("vbOrganization", organization))
						.list();
				if (userNames.isEmpty()) {
					for (VbEmployee vbEmployee : vbEmployeeList) {
						systemAlertsNotifications = new VbSystemAlertsNotifications();
						systemAlertsNotifications.setVbEmployee(vbEmployee);
						systemAlertsNotifications.setVbNotifications(vbNotifications);
						systemAlertsNotifications.setVbRole(vbRole);
						systemAlertsNotifications
								.setVbSystemAlerts(systemAlerts);

						if (OrganizationUtils.ALERT_NOTIFICATION_TYPE_EMAIL.equalsIgnoreCase(notificationType)) {
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
							_logger.debug("Persisting VbSystemAlertsNotifications: {}",	systemAlertsNotifications);
						}
						session.save(systemAlertsNotifications);
					}
				} else {
					String[] userNamesArray = userNames.split(",");
					List<String> userNamesList = Arrays.asList(userNamesArray);
					String[] userNameReceipient = null;
					for (String userName : userNamesList) {
						userNameReceipient = userName.split("/");

						VbEmployee vbEmployee = (VbEmployee) session
								.createCriteria(VbEmployee.class)
								.add(Restrictions.eq("username", userNameReceipient[0]))
								.add(Restrictions.eq("vbOrganization", organization))
								.uniqueResult();
						systemAlertsNotifications = new VbSystemAlertsNotifications();
						systemAlertsNotifications.setVbEmployee(vbEmployee);
						systemAlertsNotifications
								.setVbNotifications(vbNotifications);
						systemAlertsNotifications.setVbRole(vbRole);
						systemAlertsNotifications.setVbSystemAlerts(systemAlerts);
						if (OrganizationUtils.ALERT_NOTIFICATION_TYPE_EMAIL.equalsIgnoreCase(notificationType)) {
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
							_logger.debug("Persisting VbSystemAlertsNotifications: {}", systemAlertsNotifications);
						}
						session.save(systemAlertsNotifications);
					}
				}
			} else {
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
						systemAlertsNotifications = new VbSystemAlertsNotifications();
						systemAlertsNotifications.setVbEmployee(vbEmployee);
						systemAlertsNotifications.setVbNotifications(vbNotifications);
						systemAlertsNotifications.setVbRole(vbRole);
						systemAlertsNotifications.setVbSystemAlerts(systemAlerts);

						if (OrganizationUtils.ALERT_NOTIFICATION_TYPE_EMAIL.equalsIgnoreCase(notificationType)) {
							systemAlertsNotifications.setMailsBcc(Boolean.FALSE);
							systemAlertsNotifications.setMailsTo(Boolean.TRUE);
							systemAlertsNotifications.setMailsCc(Boolean.FALSE);
						}
						if (_logger.isDebugEnabled()) {
							_logger.debug("Persisting VbSystemAlertsNotifications: {}", systemAlertsNotifications);
						}
						session.save(systemAlertsNotifications);
					}
				}
			}
		}
		txn.commit();
		session.close();
	}

	private VbAlertCategory getAlertCategoryForSystemAlerts(Session session) {
		VbAlertCategory alertCategory = (VbAlertCategory) session
				.createCriteria(VbAlertCategory.class)
				.add(Restrictions.eq("alertCategory",
						OrganizationUtils.SYSTEM_ALERT)).uniqueResult();

		return alertCategory;
	}

	private VbAlertCategory getAlertCategoryForUserDefinedAlerts(Session session) {
		VbAlertCategory alertCategory = (VbAlertCategory) session
				.createCriteria(VbAlertCategory.class)
				.add(Restrictions.eq("alertCategory",
						OrganizationUtils.USER_DEFINED_ALERT)).uniqueResult();

		return alertCategory;
	}

	private VbAlertType getVbAlertType(Session session, String alertType,
			VbAlertCategory alertCategory) {
		VbAlertType vbAlertType = (VbAlertType) session
				.createCriteria(VbAlertType.class)
				.add(Restrictions.eq("alertType", alertType))
				.add(Restrictions.eq("vbAlertCategory", alertCategory))
				.uniqueResult();

		return vbAlertType;
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getApprovalTypes(String value,
			VbOrganization organization) {
		Session session = this.getSession();
		Integer id = (Integer) session.createCriteria(VbAlertTypeMySales.class)
				.createAlias("vbAlertType", "alerttype")
				.setProjection(Projections.property("id"))
				.add(Restrictions.eq("alertMySales", value).ignoreCase())
				.uniqueResult();
		
		List<String> approvalTypeList = session
				.createCriteria(VbAlertTypeMySalesPage.class)
				.createAlias("vbAlertTypeMySales", "alertTypeMySales")
				.setProjection(Projections.property("alertMySalesPage"))
				.add(Restrictions.eq("alertTypeMySales.id", id))
				.list();
		
		if (_logger.isDebugEnabled()) {
			_logger.debug("Retrieved Approval Types Cr Types {}", approvalTypeList);
		}
		return approvalTypeList;
	}

	public List<String> getUserDefinedAlerts() {
		Session session = this.getSession();
		VbAlertCategory vbAlertCategory = getAlertCategoryForUserDefinedAlerts(session);
		@SuppressWarnings("unchecked")
		List<String> userDefinedAlertsList = session
				.createCriteria(VbAlertType.class)
				.setProjection(Projections.property("alertType"))
				.add(Restrictions.eq("vbAlertCategory", vbAlertCategory))
				.list();
		session.close();
		if (_logger.isDebugEnabled()) {
			_logger.debug("Configured alert types are {}",
					userDefinedAlertsList);
		}
		return userDefinedAlertsList;
	}

	public List<String> getMySalesTypes() {
		Session session = this.getSession();
		@SuppressWarnings("unchecked")
		List<String> mySalesTypesList = session
				.createCriteria(VbAlertTypeMySales.class)
				.setProjection(Projections.property("alertMySales")).list();
		return mySalesTypesList;
	}

	public List<String> getTransactionTypes(String value,
			VbOrganization organization) {
		Session session = this.getSession();
		Integer id = (Integer) session.createCriteria(VbAlertTypeMySales.class)
				.createAlias("vbAlertType", "alerttype")
				.setProjection(Projections.property("id"))
				.add(Restrictions.eq("alertMySales", value).ignoreCase())
				.uniqueResult();
		@SuppressWarnings("unchecked")
		List<String> transactionTypeList = session
				.createCriteria(VbAlertTypeMySalesPage.class)
				.createAlias("vbAlertTypeMySales", "alertTypeMySales")
				.setProjection(Projections.property("alertMySalesPage"))
				.add(Restrictions.eq("alertTypeMySales.id", id)).list();
		if (_logger.isDebugEnabled()) {
			_logger.debug("Retrieved Transaction Cr Types {}",
					transactionTypeList);
		}
		return transactionTypeList;
	}

	@SuppressWarnings("unchecked")
	private List<VbSystemAlerts> getVbSystemAlerts(Session session,
			VbAlertType alertType, VbOrganization organization) {
		List<VbSystemAlerts> systemAlerts = session.createCriteria(VbSystemAlerts.class)
				.add(Restrictions.eq("vbAlertType", alertType))
				.add(Restrictions.eq("vbOrganization", organization))
				.add(Restrictions.eq("activeInactive", Boolean.TRUE)).list();

		return systemAlerts;
	}

	/**
	 * This method id responsible to get {@link VbNotifications} instance.
	 * 
	 * @param session
	 *            - {@link Session}
	 * @param notificationType
	 *            - {@link String}
	 * @return notifications - {@link VbNotifications}
	 * 
	 */
	private VbNotifications getVbNotifications(Session session,
			String notificationType) {
		VbNotifications notifications = (VbNotifications) session
				.createCriteria(VbNotifications.class)
				.add(Restrictions.eq("notificationType", notificationType))
				.uniqueResult();

		return notifications;
	}

	/**
	 * This method is responsible to get {@link VbUserDefinedAlerts} instance.
	 * 
	 * @param session
	 *            - {@link Session}
	 * @param alertType
	 *            - {@link String}
	 * @param organization
	 *            - {@link VbOrganization}
	 * @return userDefinedAlerts - {@link VbUserDefinedAlerts}
	 * 
	 */
	@SuppressWarnings("unchecked")
	private List<VbUserDefinedAlerts> getVbUserDefinedAlerts(Session session, VbAlertType alertType, VbOrganization organization) {
		List<VbUserDefinedAlerts> userDefinedAlerts = (List<VbUserDefinedAlerts>) session.createCriteria(VbUserDefinedAlerts.class)
				.add(Restrictions.eq("vbAlertType", alertType))
				.add(Restrictions.eq("vbOrganization", organization))
				.add(Restrictions.eq("activeInactive", Boolean.TRUE))
				.uniqueResult();

		return userDefinedAlerts;
	}

	@SuppressWarnings("unchecked")
	public void saveSystemDefinedAlertForMultipleGroups(
			AlertsCommand alertsCommand, HashMap<String, String> multiGroupMap,
			String notificationType, VbOrganization organization,
			String createdBy) {
		Session session = this.getSession();
		Transaction txn = session.beginTransaction();
		Date date = new Date();
		// Persisting VbSystemAlerts.
		VbAlertCategory alertCategory = getAlertCategoryForSystemAlerts(session);
		VbAlertType alertType = getVbAlertType(session,
				alertsCommand.getAlertType(), alertCategory);
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

		String[] notifications = notificationType.split(",");
		List<String> notificationList = Arrays.asList(notifications);
		VbNotifications vbNotifications = null;
		String group = null;
		String groupUsers = null;
		String[] groupUser = null;
		String[] groupNames = null;
		String userGroup = null;
		String groupReceipient = null;
		String empType = null;
		VbSystemAlertsNotifications systemAlertsNotifications = null;
		List<VbEmployee> vbEmployeeList = null;
		for (String notification : notificationList) {
			vbNotifications = (VbNotifications) session
					.createCriteria(VbNotifications.class)
					.add(Restrictions.eq("notificationType", notification))
					.uniqueResult();
			for (Map.Entry<String, String> entry : multiGroupMap.entrySet()) {
				group = entry.getKey();
				groupUsers = entry.getValue();
				groupNames = group.split("/");
				userGroup = groupNames[0];
				VbRole vbRole = (VbRole) session.createCriteria(VbRole.class)
						.add(Restrictions.eq("description", userGroup))
						.uniqueResult();
				empType = OrganizationUtils.getEmployeeTypeByRole(userGroup);
				vbEmployeeList = session.createCriteria(VbEmployee.class)
						.add(Restrictions.eq("employeeType", empType))
						.add(Restrictions.eq("vbOrganization", organization))
						.list();

				if (OrganizationUtils.ALERT_NOTIFICATION_TYPE_EMAIL
						.equalsIgnoreCase(notificationType)) {
					groupReceipient = groupNames[1];
				}
				groupUser = groupUsers.split(",");
				List<String> groupUserList = Arrays.asList(groupUser);

				if (groupUsers.isEmpty()) {
					for (VbEmployee vbEmployee : vbEmployeeList) {
						systemAlertsNotifications = new VbSystemAlertsNotifications();
						systemAlertsNotifications.setVbEmployee(vbEmployee);
						systemAlertsNotifications
								.setVbNotifications(vbNotifications);
						systemAlertsNotifications.setVbRole(vbRole);
						systemAlertsNotifications
								.setVbSystemAlerts(systemAlerts);

						if (OrganizationUtils.ALERT_NOTIFICATION_TYPE_EMAIL
								.equalsIgnoreCase(notificationType)) {
							if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_TO
									.equalsIgnoreCase(groupReceipient)) {
								systemAlertsNotifications
										.setMailsTo(Boolean.TRUE);
								systemAlertsNotifications
										.setMailsCc(Boolean.FALSE);
								systemAlertsNotifications
										.setMailsBcc(Boolean.FALSE);
							} else if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_CC
									.equalsIgnoreCase(groupReceipient)) {
								systemAlertsNotifications
										.setMailsTo(Boolean.FALSE);
								systemAlertsNotifications
										.setMailsCc(Boolean.TRUE);
								systemAlertsNotifications
										.setMailsBcc(Boolean.FALSE);
							} else if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_BCC
									.equalsIgnoreCase(groupReceipient)) {
								systemAlertsNotifications
										.setMailsTo(Boolean.FALSE);
								systemAlertsNotifications
										.setMailsCc(Boolean.FALSE);
								systemAlertsNotifications
										.setMailsBcc(Boolean.TRUE);
							} else {
								systemAlertsNotifications
										.setMailsBcc(Boolean.FALSE);
								systemAlertsNotifications
										.setMailsTo(Boolean.TRUE);
								systemAlertsNotifications
										.setMailsCc(Boolean.FALSE);
							}
						}
						if (_logger.isDebugEnabled()) {
							_logger.debug(
									"Persisting VbSystemAlertsNotifications: {}",
									systemAlertsNotifications);
						}
						session.save(systemAlertsNotifications);
					}
				} else {
					String[] userNameReceipient = null;
					for (String userName : groupUserList) {
						userNameReceipient = userName.split("/");

						VbEmployee vbEmployee = (VbEmployee) session
								.createCriteria(VbEmployee.class)
								.add(Restrictions.eq("username",
										userNameReceipient[0]))
								.add(Restrictions.eq("vbOrganization",
										organization)).uniqueResult();
						systemAlertsNotifications = new VbSystemAlertsNotifications();
						systemAlertsNotifications.setVbEmployee(vbEmployee);
						systemAlertsNotifications
								.setVbNotifications(vbNotifications);
						systemAlertsNotifications.setVbRole(vbRole);
						systemAlertsNotifications
								.setVbSystemAlerts(systemAlerts);
						if (OrganizationUtils.ALERT_NOTIFICATION_TYPE_EMAIL
								.equalsIgnoreCase(notificationType)) {
							if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_TO
									.equalsIgnoreCase(userNameReceipient[1])) {
								systemAlertsNotifications
										.setMailsTo(Boolean.TRUE);
								systemAlertsNotifications
										.setMailsCc(Boolean.FALSE);
								systemAlertsNotifications
										.setMailsBcc(Boolean.FALSE);
							} else if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_CC
									.equalsIgnoreCase(userNameReceipient[1])) {
								systemAlertsNotifications
										.setMailsTo(Boolean.FALSE);
								systemAlertsNotifications
										.setMailsCc(Boolean.TRUE);
								systemAlertsNotifications
										.setMailsBcc(Boolean.FALSE);
							} else if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_BCC
									.equalsIgnoreCase(userNameReceipient[1])) {
								systemAlertsNotifications
										.setMailsTo(Boolean.FALSE);
								systemAlertsNotifications
										.setMailsCc(Boolean.FALSE);
								systemAlertsNotifications
										.setMailsBcc(Boolean.TRUE);
							} else {
								if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_BCC
										.equalsIgnoreCase(groupReceipient)) {
									systemAlertsNotifications
											.setMailsBcc(Boolean.TRUE);
									systemAlertsNotifications
											.setMailsTo(Boolean.FALSE);
									systemAlertsNotifications
											.setMailsCc(Boolean.FALSE);
								} else if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_CC
										.equalsIgnoreCase(groupReceipient)) {
									systemAlertsNotifications
											.setMailsBcc(Boolean.FALSE);
									systemAlertsNotifications
											.setMailsTo(Boolean.FALSE);
									systemAlertsNotifications
											.setMailsCc(Boolean.TRUE);
								} else {
									systemAlertsNotifications
											.setMailsBcc(Boolean.FALSE);
									systemAlertsNotifications
											.setMailsTo(Boolean.TRUE);
									systemAlertsNotifications
											.setMailsCc(Boolean.FALSE);
								}
							}
						}

						if (_logger.isDebugEnabled()) {
							_logger.debug(
									"Persisting VbSystemAlertsNotifications For Multiple USers: {}",
									systemAlertsNotifications);
						}
						session.save(systemAlertsNotifications);
					} // closing 3rd for
				} // closing else

			} // closing 2nd for

		} // closing 1st for

		txn.commit();
		session.close();

	}

	@SuppressWarnings("unchecked")
	public void saveUserDefinedAlerts(String notificationType, String group,
			String userNames, AlertsCommand alertsCommand,
			VbUserDefinedAlertsNotifications userDefinedAlerts,
			VbOrganization vbOrganization, String userName, String salesTypes) {
		Session session = this.getSession();
		Transaction txn = session.beginTransaction();
		String groupDropdown = null;
		Date date = new Date();
		VbUserDefinedAlertsNotifications vbUserDefinedAlertsNotifications = null;
		VbAlertCategory vbAlertCategory = getAlertCategoryForUserDefinedAlerts(session);
		String alertType = alertsCommand.getAlertType();
		VbTrending vbTrending = null;
		VbExcessCash vbExcessCash =null;
		VbMySales vbMySales =null;
		VbAlertTypeMySales vbAlertTypeMySales =null;
		VbAlertTypeMySalesPage vbAlertTypeMySalesPage = null;
		List<VbAlertTypeMySalesPage> vbAlertTypeMySalesPages = null;
		VbAlertType vbAlertType =getVbAlertType(session, alertType, vbAlertCategory);
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
		session.save(vbUserDefinedAlerts);
		if (vbUserDefinedAlerts != null) {
			vbTrending = new VbTrending();
			vbExcessCash = new VbExcessCash();
			if(alertType.equals("My Sales")){
				if(salesTypes !=null){
					String[] salesTypesListArray =salesTypes.split(",");
					List<String> salesTypeList=	Arrays.asList(salesTypesListArray);
						for (String salesType : salesTypeList) {
							vbMySales = new VbMySales();
							String salesTypesvlaue = alertsCommand.getAlertMySales();
							vbAlertTypeMySales = (VbAlertTypeMySales) session.createCriteria(VbAlertTypeMySales.class)
									.add(Restrictions.eq("alertMySales",salesTypesvlaue))
									.uniqueResult();
							vbAlertTypeMySalesPage = (VbAlertTypeMySalesPage) session.createCriteria(VbAlertTypeMySalesPage.class)
									.createAlias("vbAlertTypeMySales","alertTypeMysales")
									.add(Restrictions.eq("alertMySalesPage", salesType))
									.add(Restrictions.eq("alertTypeMysales.id",vbAlertTypeMySales.getId()))
									.uniqueResult();
							vbMySales.setVbAlertTypeMySalesPage(vbAlertTypeMySalesPage);
					vbAlertType =(VbAlertType) session.createCriteria(VbAlertType.class)
							.add(Restrictions.eq("alertType", alertType))
							.uniqueResult();
					
					vbMySales.setVbAlertTypeMySales(vbAlertTypeMySales);
					vbMySales.setVbUserDefinedAlerts(vbUserDefinedAlerts);
					session.save(vbMySales);
						}
						
				
				}
				else{
					
					String salesTypesvlaue = alertsCommand.getAlertMySales();
					vbAlertTypeMySales = (VbAlertTypeMySales) session.createCriteria(VbAlertTypeMySales.class)
							.add(Restrictions.eq("alertMySales",salesTypesvlaue))
							.uniqueResult();
			vbAlertType =(VbAlertType) session.createCriteria(VbAlertType.class)
					.add(Restrictions.eq("alertType", alertType))
					.uniqueResult();
			vbAlertTypeMySalesPages = (List<VbAlertTypeMySalesPage>) session.createCriteria(VbAlertTypeMySalesPage.class)
					.createAlias("vbAlertTypeMySales","alertTypeMysales")
					.add(Restrictions.eq("alertTypeMysales.id",vbAlertTypeMySales.getId()))
					.list();
			for (VbAlertTypeMySalesPage alertTypeMySalesPage : vbAlertTypeMySalesPages) {
				vbMySales = new VbMySales();
				vbMySales.setVbAlertTypeMySalesPage(alertTypeMySalesPage);
				vbMySales.setVbAlertTypeMySales(vbAlertTypeMySales);
				vbMySales.setVbUserDefinedAlerts(vbUserDefinedAlerts);
				session.save(vbMySales);
			}
		
			txn.commit();
			
			
		}
		}else if(alertType.equals("Trending")){
				vbTrending.setAmountPersentage(alertsCommand.getAmountPercentage());
				vbTrending.setProductPercentage(alertsCommand.getProductPercentage());
				vbTrending.setDescription(alertsCommand.getDescription());
				vbTrending.setVbUserDefinedAlerts(vbUserDefinedAlerts);
				session.save(vbTrending);
			} else if (alertType.equals("Excess Amount")) {
				vbExcessCash.setAmount(alertsCommand.getAmount());
				vbExcessCash.setDescription(alertsCommand.getDescription());
				vbExcessCash.setVbUserDefinedAlerts(vbUserDefinedAlerts);
				session.save(vbExcessCash);
			}
		}

		if (_logger.isDebugEnabled()) {
			_logger.debug("Persisting VbUser Defined Alerts: {}", vbUserDefinedAlerts);
		}
		String[] notifications = notificationType.split(",");
		List<String> notificationList = Arrays.asList(notifications);
		VbNotifications vbNotifications = null;
		String[] groupReceipient = group.split("/");
		String groupName = groupReceipient[0];
		if (OrganizationUtils.ALERT_NOTIFICATION_TYPE_EMAIL
				.equalsIgnoreCase(notificationType)) {
			groupDropdown = groupReceipient[1];
		}

		VbRole vbRole = (VbRole) session.createCriteria(VbRole.class)
				.add(Restrictions.eq("description", groupName)).uniqueResult();
		String empType = OrganizationUtils.getEmployeeTypeByRole(groupName);
		List<VbEmployee> vbEmployeeList = null;
		VbSystemAlertsNotifications systemAlertsNotifications = null;
		String[] userNamesArray = userNames.split(",");
		List<String> userNamesList = Arrays.asList(userNamesArray);
		for (String notification : notificationList) {
			vbNotifications = (VbNotifications) session
					.createCriteria(VbNotifications.class)
					.add(Restrictions.eq("notificationType", notification))
					.uniqueResult();
			vbEmployeeList = session.createCriteria(VbEmployee.class)
					.add(Restrictions.eq("employeeType", empType))
					.add(Restrictions.eq("vbOrganization", vbOrganization))
					.list();
			if (userNames.isEmpty()) {
				for (VbEmployee vbEmployee : vbEmployeeList) {
					vbUserDefinedAlertsNotifications = new VbUserDefinedAlertsNotifications();
					vbUserDefinedAlertsNotifications.setVbEmployee(vbEmployee);
					vbUserDefinedAlertsNotifications
							.setVbNotifications(vbNotifications);
					vbUserDefinedAlertsNotifications.setVbRole(vbRole);
					vbUserDefinedAlertsNotifications
							.setVbUserDefinedAlerts(vbUserDefinedAlerts);

					if (OrganizationUtils.ALERT_NOTIFICATION_TYPE_EMAIL
							.equalsIgnoreCase(notificationType)) {
						if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_TO
								.equalsIgnoreCase(groupDropdown)) {
							vbUserDefinedAlertsNotifications
									.setMailsTo(Boolean.TRUE);
							vbUserDefinedAlertsNotifications
									.setMailsCc(Boolean.FALSE);
							vbUserDefinedAlertsNotifications
									.setMailsBcc(Boolean.FALSE);
						} else if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_CC
								.equalsIgnoreCase(groupDropdown)) {
							vbUserDefinedAlertsNotifications
									.setMailsTo(Boolean.FALSE);
							vbUserDefinedAlertsNotifications
									.setMailsCc(Boolean.TRUE);
							vbUserDefinedAlertsNotifications
									.setMailsBcc(Boolean.FALSE);
						} else if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_BCC
								.equalsIgnoreCase(groupDropdown)) {
							vbUserDefinedAlertsNotifications
									.setMailsTo(Boolean.FALSE);
							vbUserDefinedAlertsNotifications
									.setMailsCc(Boolean.FALSE);
							vbUserDefinedAlertsNotifications
									.setMailsBcc(Boolean.TRUE);
						} else {
							vbUserDefinedAlertsNotifications
									.setMailsBcc(Boolean.FALSE);
							vbUserDefinedAlertsNotifications
									.setMailsTo(Boolean.TRUE);
							vbUserDefinedAlertsNotifications
									.setMailsCc(Boolean.FALSE);
						}
					}
					if (_logger.isDebugEnabled()) {
						_logger.debug(
								"Persisting VbSystemAlertsNotifications: {}",
								systemAlertsNotifications);
					}
					session.save(vbUserDefinedAlertsNotifications);
				}
			} else {
				String[] userNameReceipient = null;
				for (String userName1 : userNamesList) {
					userNameReceipient = userName1.split("/");

					VbEmployee vbEmployee = (VbEmployee) session
							.createCriteria(VbEmployee.class)
							.add(Restrictions.eq("username",
									userNameReceipient[0]))
							.add(Restrictions.eq("vbOrganization",
									vbOrganization)).uniqueResult();
					vbUserDefinedAlertsNotifications = new VbUserDefinedAlertsNotifications();
					vbUserDefinedAlertsNotifications.setVbEmployee(vbEmployee);
					vbUserDefinedAlertsNotifications
							.setVbNotifications(vbNotifications);
					vbUserDefinedAlertsNotifications.setVbRole(vbRole);
					vbUserDefinedAlertsNotifications
							.setVbUserDefinedAlerts(vbUserDefinedAlerts);
					if (OrganizationUtils.ALERT_NOTIFICATION_TYPE_EMAIL
							.equalsIgnoreCase(notificationType)) {
						if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_TO
								.equalsIgnoreCase(userNameReceipient[1])) {
							vbUserDefinedAlertsNotifications
									.setMailsTo(Boolean.TRUE);
							vbUserDefinedAlertsNotifications
									.setMailsCc(Boolean.FALSE);
							vbUserDefinedAlertsNotifications
									.setMailsBcc(Boolean.FALSE);
						} else if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_CC
								.equalsIgnoreCase(userNameReceipient[1])) {
							vbUserDefinedAlertsNotifications
									.setMailsTo(Boolean.FALSE);
							vbUserDefinedAlertsNotifications
									.setMailsCc(Boolean.TRUE);
							vbUserDefinedAlertsNotifications
									.setMailsBcc(Boolean.FALSE);
						} else if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_BCC
								.equalsIgnoreCase(userNameReceipient[1])) {
							vbUserDefinedAlertsNotifications
									.setMailsTo(Boolean.FALSE);
							vbUserDefinedAlertsNotifications
									.setMailsCc(Boolean.FALSE);
							vbUserDefinedAlertsNotifications
									.setMailsBcc(Boolean.TRUE);
						} else {
							if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_BCC
									.equalsIgnoreCase(groupDropdown)) {
								vbUserDefinedAlertsNotifications
										.setMailsBcc(Boolean.TRUE);
								vbUserDefinedAlertsNotifications
										.setMailsTo(Boolean.FALSE);
								vbUserDefinedAlertsNotifications
										.setMailsCc(Boolean.FALSE);
							} else if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_CC
									.equalsIgnoreCase(groupDropdown)) {
								vbUserDefinedAlertsNotifications
										.setMailsBcc(Boolean.FALSE);
								vbUserDefinedAlertsNotifications
										.setMailsTo(Boolean.FALSE);
								vbUserDefinedAlertsNotifications
										.setMailsCc(Boolean.TRUE);
							} else {
								vbUserDefinedAlertsNotifications
										.setMailsBcc(Boolean.FALSE);
								vbUserDefinedAlertsNotifications
										.setMailsTo(Boolean.TRUE);
								vbUserDefinedAlertsNotifications
										.setMailsCc(Boolean.FALSE);
							}
						}
					}

					if (_logger.isDebugEnabled()) {
						_logger.debug(
								"Persisting VbSystemAlertsNotifications: {}",
								vbUserDefinedAlertsNotifications);
					}
					session.save(vbUserDefinedAlertsNotifications);
				}
				//txn.commit();

			}

		}
	}

	@SuppressWarnings("unchecked")
	public void saveUserDefinedAlertForMultipleGroups(
			AlertsCommand alertsCommand, HashMap<String, String> multiGroupMap,
			String notificationType, VbOrganization organization,
			String createdBy) {
		Session session = this.getSession();
		Transaction txn = session.beginTransaction();
		Date date = new Date();
		// Persisting VbSystemAlerts.
		VbAlertCategory alertCategory = getAlertCategoryForUserDefinedAlerts(session);
		VbAlertType alertType = getVbAlertType(session,
				alertsCommand.getAlertType(), alertCategory);
		VbUserDefinedAlerts vbUserDefinedAlerts = new VbUserDefinedAlerts();
		vbUserDefinedAlerts.setAlertName(alertsCommand.getAlertName());
		vbUserDefinedAlerts.setActiveInactive(Boolean.TRUE);
		vbUserDefinedAlerts.setCreatedBy(createdBy);
		vbUserDefinedAlerts.setCreatedOn(date);
		vbUserDefinedAlerts.setModifiedOn(date);
		vbUserDefinedAlerts.setDescription(alertsCommand.getDescription());
		vbUserDefinedAlerts.setVbAlertType(alertType);
		vbUserDefinedAlerts.setVbOrganization(organization);

		if (_logger.isDebugEnabled()) {
			_logger.debug("Persisting VbSystemAlerts: {}", vbUserDefinedAlerts);
		}
		session.save(vbUserDefinedAlerts);

		String[] notifications = notificationType.split(",");
		List<String> notificationList = Arrays.asList(notifications);
		VbNotifications vbNotifications = null;
		String group = null;
		String groupUsers = null;
		String[] groupUser = null;
		String[] groupNames = null;
		String userGroup = null;
		String groupReceipient = null;
		String empType = null;
		VbUserDefinedAlertsNotifications userDefinedAlertsNotifications = null;
		List<VbEmployee> vbEmployeeList = null;
		for (String notification : notificationList) {
			vbNotifications = (VbNotifications) session
					.createCriteria(VbNotifications.class)
					.add(Restrictions.eq("notificationType", notification))
					.uniqueResult();
			for (Map.Entry<String, String> entry : multiGroupMap.entrySet()) {
				group = entry.getKey();
				groupUsers = entry.getValue();
				groupNames = group.split("/");
				userGroup = groupNames[0];
				VbRole vbRole = (VbRole) session.createCriteria(VbRole.class)
						.add(Restrictions.eq("description", userGroup))
						.uniqueResult();
				empType = OrganizationUtils.getEmployeeTypeByRole(userGroup);
				vbEmployeeList = session.createCriteria(VbEmployee.class)
						.add(Restrictions.eq("employeeType", empType))
						.add(Restrictions.eq("vbOrganization", organization))
						.list();

				if (OrganizationUtils.ALERT_NOTIFICATION_TYPE_EMAIL
						.equalsIgnoreCase(notificationType)) {
					groupReceipient = groupNames[1];
				}
				groupUser = groupUsers.split(",");
				List<String> groupUserList = Arrays.asList(groupUser);

				if (groupUsers.isEmpty()) {
					for (VbEmployee vbEmployee : vbEmployeeList) {
						userDefinedAlertsNotifications = new VbUserDefinedAlertsNotifications();
						userDefinedAlertsNotifications
								.setVbEmployee(vbEmployee);
						userDefinedAlertsNotifications
								.setVbNotifications(vbNotifications);
						userDefinedAlertsNotifications.setVbRole(vbRole);
						userDefinedAlertsNotifications
								.setVbUserDefinedAlerts(vbUserDefinedAlerts);

						if (OrganizationUtils.ALERT_NOTIFICATION_TYPE_EMAIL
								.equalsIgnoreCase(notificationType)) {
							if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_TO
									.equalsIgnoreCase(groupReceipient)) {
								userDefinedAlertsNotifications
										.setMailsTo(Boolean.TRUE);
								userDefinedAlertsNotifications
										.setMailsCc(Boolean.FALSE);
								userDefinedAlertsNotifications
										.setMailsBcc(Boolean.FALSE);
							} else if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_CC
									.equalsIgnoreCase(groupReceipient)) {
								userDefinedAlertsNotifications
										.setMailsTo(Boolean.FALSE);
								userDefinedAlertsNotifications
										.setMailsCc(Boolean.TRUE);
								userDefinedAlertsNotifications
										.setMailsBcc(Boolean.FALSE);
							} else if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_BCC
									.equalsIgnoreCase(groupReceipient)) {
								userDefinedAlertsNotifications
										.setMailsTo(Boolean.FALSE);
								userDefinedAlertsNotifications
										.setMailsCc(Boolean.FALSE);
								userDefinedAlertsNotifications
										.setMailsBcc(Boolean.TRUE);
							} else {
								userDefinedAlertsNotifications
										.setMailsBcc(Boolean.FALSE);
								userDefinedAlertsNotifications
										.setMailsTo(Boolean.TRUE);
								userDefinedAlertsNotifications
										.setMailsCc(Boolean.FALSE);
							}
						}
						if (_logger.isDebugEnabled()) {
							_logger.debug(
									"Persisting VbSystemAlertsNotifications: {}",
									userDefinedAlertsNotifications);
						}
						session.save(userDefinedAlertsNotifications);
					}
				} else {
					String[] userNameReceipient = null;
					for (String userName : groupUserList) {
						userNameReceipient = userName.split("/");

						VbEmployee vbEmployee = (VbEmployee) session
								.createCriteria(VbEmployee.class)
								.add(Restrictions.eq("username",
										userNameReceipient[0]))
								.add(Restrictions.eq("vbOrganization",
										organization)).uniqueResult();
						userDefinedAlertsNotifications = new VbUserDefinedAlertsNotifications();
						userDefinedAlertsNotifications
								.setVbEmployee(vbEmployee);
						userDefinedAlertsNotifications
								.setVbNotifications(vbNotifications);
						userDefinedAlertsNotifications.setVbRole(vbRole);
						userDefinedAlertsNotifications
								.setVbUserDefinedAlerts(vbUserDefinedAlerts);
						if (OrganizationUtils.ALERT_NOTIFICATION_TYPE_EMAIL
								.equalsIgnoreCase(notificationType)) {
							if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_TO
									.equalsIgnoreCase(userNameReceipient[1])) {
								userDefinedAlertsNotifications
										.setMailsTo(Boolean.TRUE);
								userDefinedAlertsNotifications
										.setMailsCc(Boolean.FALSE);
								userDefinedAlertsNotifications
										.setMailsBcc(Boolean.FALSE);
							} else if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_CC
									.equalsIgnoreCase(userNameReceipient[1])) {
								userDefinedAlertsNotifications
										.setMailsTo(Boolean.FALSE);
								userDefinedAlertsNotifications
										.setMailsCc(Boolean.TRUE);
								userDefinedAlertsNotifications
										.setMailsBcc(Boolean.FALSE);
							} else if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_BCC
									.equalsIgnoreCase(userNameReceipient[1])) {
								userDefinedAlertsNotifications
										.setMailsTo(Boolean.FALSE);
								userDefinedAlertsNotifications
										.setMailsCc(Boolean.FALSE);
								userDefinedAlertsNotifications
										.setMailsBcc(Boolean.TRUE);
							} else {
								if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_BCC
										.equalsIgnoreCase(groupReceipient)) {
									userDefinedAlertsNotifications
											.setMailsBcc(Boolean.TRUE);
									userDefinedAlertsNotifications
											.setMailsTo(Boolean.FALSE);
									userDefinedAlertsNotifications
											.setMailsCc(Boolean.FALSE);
								} else if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_CC
										.equalsIgnoreCase(groupReceipient)) {
									userDefinedAlertsNotifications
											.setMailsBcc(Boolean.FALSE);
									userDefinedAlertsNotifications
											.setMailsTo(Boolean.FALSE);
									userDefinedAlertsNotifications
											.setMailsCc(Boolean.TRUE);
								} else {
									userDefinedAlertsNotifications
											.setMailsBcc(Boolean.FALSE);
									userDefinedAlertsNotifications
											.setMailsTo(Boolean.TRUE);
									userDefinedAlertsNotifications
											.setMailsCc(Boolean.FALSE);
								}
							}
						}

						if (_logger.isDebugEnabled()) {
							_logger.debug(
									"Persisting VbSystemAlertsNotifications For Multiple USers: {}",
									userDefinedAlertsNotifications);
						}
						session.save(userDefinedAlertsNotifications);
					} // closing 3rd for
				} // closing else

			} // closing 2nd for

		} // closing 1st for

		txn.commit();
		session.close();

	}

	/**
	 * This method is responsible to get the configured alert categories.
	 * 
	 * @return alertCategoryList - {@link List}
	 */
	@SuppressWarnings("unchecked")
	public List<String> getAlertCategory() {
		Session session = this.getSession();
		List<String> alertCategoryList = session
				.createCriteria(VbAlertCategory.class)
				.setProjection(Projections.property("alertCategory")).list();
		session.close();
		if (_logger.isDebugEnabled()) {
			_logger.debug("Configured alert categories are {}",
					alertCategoryList);
		}
		return alertCategoryList;
	}

	/**
	 * This method is responsible to get the alerttypes based on the
	 * alertcategory.
	 * 
	 * @param alertCategory
	 *            - {@link String}
	 * @return alertTypeList - {@link List}
	 */
	@SuppressWarnings("unchecked")
	public List<String> getAlertType(String alertCategory) {
		Session session = this.getSession();
		VbAlertCategory vbAlertCategory = (VbAlertCategory) session
				.createCriteria(VbAlertCategory.class)
				.add(Restrictions.eq("alertCategory", alertCategory))
				.uniqueResult();
		List<String> alertTypeList = session.createCriteria(VbAlertType.class)
				.setProjection(Projections.property("alertType"))
				.add(Restrictions.eq("vbAlertCategory", vbAlertCategory))
				.list();
		session.close();
		if (_logger.isDebugEnabled()) {
			_logger.debug(
					"Configured alert types {} for alertCategory {} are: ",
					alertTypeList, alertCategory);
		}
		return alertTypeList;
	}

	/**
	 * This method is responsible to get all the configured alerts.
	 * 
	 * @return alertResultList - {@link List}
	 */
	@SuppressWarnings("unchecked")
	public List<ViewAlertResult> getAllAlerts(String alertCategory, String alertType, VbOrganization organization) {
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
		
		for (VbSystemAlerts vbSystemAlerts : systemAlertsList) {
			alertResult = new ViewAlertResult();
			alertResult.setId(vbSystemAlerts.getId());
			alertResult.setAlertCategory(vbSystemAlerts.getVbAlertType().getVbAlertCategory().getAlertCategory());
			alertResult.setAlertName(vbSystemAlerts.getAlertName());
			alertResult.setAlertType(vbSystemAlerts.getVbAlertType().getAlertType());
			alertResult.setAlertDescription(vbSystemAlerts.getDescription());
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
		}
		else {
			userDefinedAlertsList = session.createQuery("FROM VbUserDefinedAlerts vb WHERE vb.vbAlertType.vbAlertCategory.alertCategory = :alertCategory AND vb.vbAlertType.alertType = :alertType AND vb.vbOrganization = :organization")
					.setParameter("alertCategory", alertCategory)
					.setParameter("alertType", alertType)
					.setParameter("organization", organization)
					.list();
		}
		
		
		for (VbUserDefinedAlerts vbUserDefinedAlerts : userDefinedAlertsList) {
			alertResult = new ViewAlertResult();
			String alertCriteria = null;
			String alertTypeVal = vbUserDefinedAlerts.getVbAlertType().getAlertType();
			
			if(alertTypeVal.equals("Trending")) {
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
				VbMySales vbMySales = (VbMySales) session.createCriteria(VbMySales.class)
						.add(Restrictions.eq("vbUserDefinedAlerts", vbUserDefinedAlerts))
						.uniqueResult();
				alertCriteria = "Mysales:" +vbMySales.getVbAlertTypeMySales().getAlertMySales();
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
		if(_logger.isDebugEnabled()) {
			_logger.debug("Configured alerts list size is: {}", alertResultList.size());
		}
		return alertResultList;
	}
	
	

	/**
	 * This method is responsible to get {@link VbTrending} instance.
	 * 
	 * @param alertType - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return vbTrending - {@link VbTrending}
	 * 
	 */
	public VbTrending getVbTrending(String alertType, VbOrganization organization) {
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
		
		if(_logger.isDebugEnabled()) {
			_logger.debug("VbTrending: {}", vbTrending);
		}
		return vbTrending;
	}
	
	/**
	 * This method is responsible to get today's and yesterday's soldQty from
	 * {@link VbSalesBookProducts} based on SalesExecutive and
	 * {@link VbOrganization}.
	 * 
	 * @param salesExecutive - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return amountTrendingResult - {@link TrendingAlertResult}
	 * 
	 */
	public TrendingAlertResult getProductTrendingAlertResult(VbOrganization organization){
		TrendingAlertResult productTrendingResult = new TrendingAlertResult();
		Session session = this.getSession();
		Date date = new Date();
		
		// For Yesterday's SoldQty.
		VbSalesBook yesterdaySalesBook = getVbSalesBook(session, organization, DateUtils.getYestersDayDate(date));
		Integer yesterdaySoldQty = (Integer) session.createCriteria(VbSalesBookProducts.class)
				.setProjection(Projections.sum("qtySold"))
				.add(Restrictions.eq("vbSalesBook", yesterdaySalesBook))
				.add(Restrictions.eq("vbOrganization", organization))
				.uniqueResult();
		productTrendingResult.setYesterdaySoldQty(yesterdaySoldQty);
		
		// For Today's SoldQty.
		VbSalesBook todaySalesBook = getVbSalesBook(session, organization, date);
		Integer todaySoldQty = (Integer) session.createCriteria(VbSalesBookProducts.class)
				.setProjection(Projections.sum("qtySold"))
				.add(Restrictions.eq("vbSalesBook", todaySalesBook))
				.add(Restrictions.eq("vbOrganization", organization))
				.uniqueResult();
		productTrendingResult.setTodaySoldQty(todaySoldQty);
		session.close();
		
		if(_logger.isDebugEnabled()) {
			_logger.debug("TrendingAlertResult: {}", productTrendingResult);
		}
		return productTrendingResult;
	}
	
	/**
	 * This method is responsible to get today's and yesterday's closing balance
	 * based on SalesExecutive and {@link VbOrganization}.
	 * 
	 * @param organization - {@link VbOrganization}
	 * @return amountTrendingResult - {@link TrendingAlertResult}
	 * 
	 */
	public TrendingAlertResult getAmountTrendingAlertResult(VbOrganization organization) {
		TrendingAlertResult amountTrendingResult = new TrendingAlertResult();
		Session session = this.getSession();
		Date date = new Date();
		
		// For Yesterday's Amount.
		Float yesterdaysAmount = (Float) session.createCriteria(VbSalesBook.class)
				.setProjection(Projections.property("closingBalance"))
				.add(Restrictions.eq("vbOrganization", organization))
				.add(Restrictions.eq("createdOn", DateUtils.getYestersDayDate(date)))
				.uniqueResult();
		amountTrendingResult.setYesterdaysAmount(yesterdaysAmount);
		
		// For Today's Amount.
		Float todaysAmount = (Float) session.createCriteria(VbSalesBook.class)
				.setProjection(Projections.property("closingBalance"))
				.add(Restrictions.eq("vbOrganization", organization))
				.add(Restrictions.eq("createdOn", date))
				.uniqueResult();
		amountTrendingResult.setTodaysAmount(todaysAmount);
		session.close();
		
		if(_logger.isDebugEnabled()) {
			_logger.debug("TrendingAlertResult: {}", amountTrendingResult);
		}
		return amountTrendingResult;
	}
	
	/**
	 * This method is responsible to get {@link VbSalesBook} based on {@link Date}.
	 * 
	 * @param session - {@link Session}
	 * @param salesExecutive - {@link String}
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
	public VbSystemAlerts getSystemAlertsData(int id){
		VbSystemAlerts vbSystemAlerts =null;
		Session session =this.getSession();
		vbSystemAlerts =(VbSystemAlerts) session.get(VbSystemAlerts.class,id);
		return vbSystemAlerts;
		
	}
	public VbUserDefinedAlerts getUserAlertsData(int id){
		VbUserDefinedAlerts userDefinedAlerts =null;
		Session session = this.getSession();
		userDefinedAlerts =(VbUserDefinedAlerts) session.get(VbUserDefinedAlerts.class, id);
		return userDefinedAlerts;
		
	}

	@SuppressWarnings("unchecked")
	public void deleteSystemAlerts(int id) {
		Session session =this.getSession();
		Transaction transaction = session.beginTransaction();
		VbSystemAlerts vbSystemAlerts =null;
	List<VbSystemAlertsNotifications> systemAlertsNotificationsList =null;
		vbSystemAlerts =(VbSystemAlerts) session.get(VbSystemAlerts.class, id);
		if(vbSystemAlerts!=null){
			systemAlertsNotificationsList = (List<VbSystemAlertsNotifications>) session.createCriteria(VbSystemAlertsNotifications.class)
					.add(Restrictions.eq("vbSystemAlerts", vbSystemAlerts))
					.list();
			for (VbSystemAlertsNotifications vbSystemAlertsNotifications : systemAlertsNotificationsList) {
				
				if(vbSystemAlertsNotifications != null){
					session.delete(vbSystemAlertsNotifications);
				}
			}
			
			session.delete(vbSystemAlerts);
		}
		transaction .commit();
		session.close();
		
		
		
	}
	@SuppressWarnings("unchecked")
	public void deleteUserAlerts(int id) {
		Session session =this.getSession();
		Transaction transaction = session.beginTransaction();
		VbUserDefinedAlerts vbUserDefinedAlerts =null;
		List<VbUserDefinedAlertsNotifications> userDefinedAlertsNotifications =null;
		vbUserDefinedAlerts =(VbUserDefinedAlerts) session.get(VbUserDefinedAlerts.class, id);
		if(vbUserDefinedAlerts!=null){
			userDefinedAlertsNotifications =(List<VbUserDefinedAlertsNotifications>) session.createCriteria(VbUserDefinedAlertsNotifications.class)
					.add(Restrictions.eq("vbUserDefinedAlerts",vbUserDefinedAlerts)).list();
			for (VbUserDefinedAlertsNotifications vbUserDefinedAlertsNotifications : userDefinedAlertsNotifications) {
				if(vbUserDefinedAlertsNotifications !=null){
					session.delete(vbUserDefinedAlertsNotifications);
				}
			}
			session.delete(vbUserDefinedAlerts);
		}
		transaction .commit();
		session.close();
		
		
		
	}

	/**
	 * This method is responsible for enabling or disabling the alert.
	 * 
	 * @param id - {@link Integer}
	 * @param status - {@link Boolean}
	 */
	public void enableOrDisableAlert(Integer id, Boolean status, String category) {
		Session session = this.getSession();
		Transaction txn = session.beginTransaction();
		if(category.equals("System Alerts")) {
			VbSystemAlerts vbSystemAlerts = (VbSystemAlerts) session.createCriteria(VbSystemAlerts.class)
					.add(Restrictions.eq("id", id))
					.uniqueResult();
			if(vbSystemAlerts != null) {
				vbSystemAlerts.setActiveInactive(status);
				
				if (_logger.isDebugEnabled()) {
					_logger.debug("Updating VbSystemAlerts: {}", vbSystemAlerts);
				}
				session.update(vbSystemAlerts);
				txn.commit();
			} else {
				if(_logger.isErrorEnabled()) {
					_logger.debug("VbSystemAlerts not found to update.");
				}
			}
		} else {
			VbUserDefinedAlerts vbUserDefinedAlerts = (VbUserDefinedAlerts) session.createCriteria(VbUserDefinedAlerts.class)
					.add(Restrictions.eq("id", id))
					.uniqueResult();
			if(vbUserDefinedAlerts != null) {
				vbUserDefinedAlerts.setActiveInactive(status);
				
				if (_logger.isDebugEnabled()) {
					_logger.debug("Updating VbUserDefinedAlerts: {}", vbUserDefinedAlerts);
				}
				session.update(vbUserDefinedAlerts);
				txn.commit();
			} else {
				if(_logger.isErrorEnabled()) {
					_logger.debug("VbUserDefinedAlerts not found to update.");
				}
			}
		}
		
		session.close();
	}

	/**
	 * 
	 * @return
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<VbOrganization> getAllOrganizations() {
		Session session = this.getSession();
		List<VbOrganization> organizationsList = session.createCriteria(VbOrganization.class)
				.add(Restrictions.ne("description", "Organization Group"))
				.list();
		session.close();
		return organizationsList;
	}
}
