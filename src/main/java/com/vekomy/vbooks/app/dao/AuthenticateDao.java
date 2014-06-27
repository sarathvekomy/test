/**
 * com.vekomy.vbooks.app.dao.AuthenticateDao.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Jul 16, 2013
 */
package com.vekomy.vbooks.app.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vekomy.vbooks.app.dao.base.BaseDao;
import com.vekomy.vbooks.app.exception.DataAccessException;
import com.vekomy.vbooks.app.request.LoginRequest;
import com.vekomy.vbooks.app.request.LoginRequestForCAU;
import com.vekomy.vbooks.app.request.PasswordChangeRequest;
import com.vekomy.vbooks.app.response.LoginResponse;
import com.vekomy.vbooks.app.response.LoginResponseForCAU;
import com.vekomy.vbooks.app.response.Response;
import com.vekomy.vbooks.app.response.SystemNotification;
import com.vekomy.vbooks.app.response.SystemNotificationList;
import com.vekomy.vbooks.app.utils.ApplicationConstants;
import com.vekomy.vbooks.app.utils.DateUtils;
import com.vekomy.vbooks.app.utils.PasswordEncryption;
import com.vekomy.vbooks.hibernate.model.VbAuthority;
import com.vekomy.vbooks.hibernate.model.VbEmployee;
import com.vekomy.vbooks.hibernate.model.VbJournalTypes;
import com.vekomy.vbooks.hibernate.model.VbLogin;
import com.vekomy.vbooks.hibernate.model.VbLoginTrack;
import com.vekomy.vbooks.hibernate.model.VbOrganization;
import com.vekomy.vbooks.hibernate.model.VbPaymentTypes;
import com.vekomy.vbooks.hibernate.model.VbRole;
import com.vekomy.vbooks.hibernate.model.VbSalesBook;
import com.vekomy.vbooks.hibernate.model.VbSystemNotifications;

/**
 * @author NKR
 * 
 */
public class AuthenticateDao extends BaseDao {
	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(AuthenticateDao.class);

	/**
	 * This method is responsible to authenticate the user credentials.
	 * 
	 * @param request - {@link LoginRequest}
	 * @return response - {@link LoginResponse}
	 */
	@SuppressWarnings("unchecked")
	public LoginResponse authenticate(LoginRequest request) {
		LoginResponse response = new LoginResponse();
		Boolean isFirstTimeLogin = Boolean.FALSE;
		try {
			Session session = this.getSession();
			String userName = request.getUserName();
			VbLogin login = (VbLogin) session.createCriteria(VbLogin.class)
					.add(Restrictions.eq("username", userName))
					.add(Restrictions.eq("password", PasswordEncryption.encryptPassword(request.getPassword())))
					.add(Restrictions.eq("enabled", ApplicationConstants.LOGIN_ENABLED))
					.uniqueResult();

			if (login != null) {
				VbRole role = (VbRole) session.get(VbRole.class,ApplicationConstants.SALES_EXECUTIVE_ROLE);
				List<VbAuthority> authorities = session.createCriteria(VbAuthority.class)
						.add(Restrictions.eq("vbLogin", login))
						.add(Restrictions.eq("vbRole", role))
						.list();
				VbOrganization organization = login.getVbOrganization();

				// Saving LoginTrack Details ---> START.
				VbLoginTrack vbLoginTrack = new VbLoginTrack();
				vbLoginTrack.setUsername(userName);
				vbLoginTrack.setLastLoginTime(DateUtils.formatDateWithTimestamp(new Date()));
				vbLoginTrack.setVbOrganization(organization);

				if (_logger.isDebugEnabled()) {
					_logger.debug("Saving vbLoginTrack");
				}
				Transaction txn = session.beginTransaction();
				session.save(vbLoginTrack);
				txn.commit();
				// Saving LoginTrack Details ---> END.

				Integer grantedDays = (Integer) session.createCriteria(VbEmployee.class)
						.setProjection(Projections.property("grantedDays"))
						.add(Restrictions.eq("username", userName))
						.add(Restrictions.eq("vbOrganization", organization))
						.uniqueResult();
				VbSalesBook salesBook = getVbSalesBook(session, userName, organization);
				if (!authorities.isEmpty()) {
					response.setStatusCode(new Integer(200));
					response.setMessage("Login and Authorisation Success");
					response.setUserName(userName);
					response.setGrandedDays(String.valueOf(grantedDays));
					if (salesBook != null) {
						response.setAllotmentType(salesBook.getAllotmentType());
					} else {
						response.setAllotmentType("");
					}
					response.setOrganizationId(organization.getId());
					Character isFirstTime = login.getFirstTime();
					if (isFirstTime == ApplicationConstants.FIRST_TIME_LOGIN_YES) {
						isFirstTimeLogin = Boolean.TRUE;
					}
					response.setFirstTimeLogin(isFirstTimeLogin);
									
					// For Configured Journal types (System LookUps).
					List<String> journalTypesList = session.createCriteria(VbJournalTypes.class)
							.setProjection(Projections.property("journalType"))
							.add(Restrictions.eq("vbOrganization", organization))
							.list();
					if (!journalTypesList.isEmpty()) {
						StringBuffer journalTypes = null;
						for (String journalType : journalTypesList) {
							if (journalTypes == null) {
								journalTypes = new StringBuffer(journalType);
							} else {
								journalTypes = journalTypes.append(",").append(journalType);
							}
						}
						response.setJournalTypes(journalTypes.toString());
					}

					// For Configured payment types (System LookUps).
					List<String> paymentTypesList = session.createCriteria(VbPaymentTypes.class)
							.setProjection(Projections.property("paymentType"))
							.add(Restrictions.eq("vbOrganization", organization))
							.list();
					if (!paymentTypesList.isEmpty()) {
						StringBuffer paymentTypes = null;
						for (String paymentType : paymentTypesList) {
							if (paymentTypes == null) {
								paymentTypes = new StringBuffer(paymentType);
							} else {
								paymentTypes = paymentTypes.append(",").append(paymentType);
							}
						}
						response.setPaymentTypes(paymentTypes.toString());
					}
				} else {
					response.setStatusCode(new Integer(1));
					response.setMessage("Authorisation Fail");
				}
			} else {
				response.setStatusCode(new Integer(2));
				response.setMessage("Authentication Fail");
			}

			if (_logger.isDebugEnabled()) {
				_logger.debug("LoginResponse: {}", response);
			}
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatusCode(10);
		}
		return response;
	}
	
	/////////////////////////////THIS IS A TEMP METHOD FOR WORK AROUND---->START///////////////////////////////
	/**
	 * This method is responsible to authenticate the user credentials.
	 * 
	 * @param request - {@link LoginRequest}
	 * @return response - {@link LoginResponse}
	 */
	@SuppressWarnings("unchecked")
	public LoginResponseForCAU authenticateForCAU(LoginRequestForCAU request) {
		LoginResponseForCAU response = new LoginResponseForCAU();
		Boolean isFirstTimeLogin = Boolean.FALSE;
		try {
			Session session = this.getSession();
			String userName = request.getUserName();
			VbLogin login = (VbLogin) session.createCriteria(VbLogin.class)
					.add(Restrictions.eq("username", userName))
					.add(Restrictions.eq("password", PasswordEncryption.encryptPassword(request.getPassword())))
					.add(Restrictions.eq("enabled", ApplicationConstants.LOGIN_ENABLED))
					.uniqueResult();

			if (login != null) {
				VbRole role = (VbRole) session.get(VbRole.class,ApplicationConstants.SALES_EXECUTIVE_ROLE);
				List<VbAuthority> authorities = session.createCriteria(VbAuthority.class)
						.add(Restrictions.eq("vbLogin", login))
						.add(Restrictions.eq("vbRole", role))
						.list();
				VbOrganization organization = login.getVbOrganization();

				// Saving LoginTrack Details ---> START.
				VbLoginTrack vbLoginTrack = new VbLoginTrack();
				vbLoginTrack.setUsername(userName);
				vbLoginTrack.setLastLoginTime(DateUtils.formatDateWithTimestamp(new Date()));
				vbLoginTrack.setVbOrganization(organization);

				if (_logger.isDebugEnabled()) {
					_logger.debug("Saving vbLoginTrack");
				}
				Transaction txn = session.beginTransaction();
				session.save(vbLoginTrack);
				txn.commit();
				// Saving LoginTrack Details ---> END.

				Integer grantedDays = (Integer) session.createCriteria(VbEmployee.class)
						.setProjection(Projections.property("grantedDays"))
						.add(Restrictions.eq("username", userName))
						.add(Restrictions.eq("vbOrganization", organization))
						.uniqueResult();
				VbSalesBook salesBook = getVbSalesBook(session, userName, organization);
				if (!authorities.isEmpty()) {
					response.setStatusCode(new Integer(200));
					response.setMessage("Login and Authorisation Success");
					response.setUserName(userName);
					response.setGrandedDays(String.valueOf(grantedDays));
					if (salesBook != null) {
						response.setAllotmentType(salesBook.getAllotmentType());
					} else {
						response.setAllotmentType("");
					}
					response.setOrganizationId(organization.getId());
					Character isFirstTime = login.getFirstTime();
					if (isFirstTime == ApplicationConstants.FIRST_TIME_LOGIN_YES) {
						isFirstTimeLogin = Boolean.TRUE;
					}
					response.setFirstTimeLogin(isFirstTimeLogin);
					String versionId = request.getVersion();
					if(versionId != null) {
						if (_logger.isDebugEnabled()) {
							_logger.debug("SLE Login Request came from Android with user name = {} with version id = {}", userName, versionId );
						}
						setAPKVersionInfo(response);
					}
					
					// For Configured Journal types (System LookUps).
					List<String> journalTypesList = session.createCriteria(VbJournalTypes.class)
							.setProjection(Projections.property("journalType"))
							.add(Restrictions.eq("vbOrganization", organization))
							.list();
					if (!journalTypesList.isEmpty()) {
						StringBuffer journalTypes = null;
						for (String journalType : journalTypesList) {
							if (journalTypes == null) {
								journalTypes = new StringBuffer(journalType);
							} else {
								journalTypes = journalTypes.append(",").append(journalType);
							}
						}
						response.setJournalTypes(journalTypes.toString());
					}

					// For Configured payment types (System LookUps).
					List<String> paymentTypesList = session.createCriteria(VbPaymentTypes.class)
							.setProjection(Projections.property("paymentType"))
							.add(Restrictions.eq("vbOrganization", organization))
							.list();
					if (!paymentTypesList.isEmpty()) {
						StringBuffer paymentTypes = null;
						for (String paymentType : paymentTypesList) {
							if (paymentTypes == null) {
								paymentTypes = new StringBuffer(paymentType);
							} else {
								paymentTypes = paymentTypes.append(",").append(paymentType);
							}
						}
						response.setPaymentTypes(paymentTypes.toString());
					}
				} else {
					response.setStatusCode(new Integer(1));
					response.setMessage("Authorisation Fail");
				}
			} else {
				response.setStatusCode(new Integer(2));
				response.setMessage("Authentication Fail");
			}

			if (_logger.isDebugEnabled()) {
				_logger.debug("LoginResponse: {}", response);
			}
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatusCode(10);
		}
		return response;
	}
	/////////////////////////////THIS IS A TEMP METHOD FOR WORK AROUND---->END///////////////////////////////
	
	@SuppressWarnings("unchecked")
	public SystemNotificationList getAllSystemNotications(
			Integer organizationId, String salesExecutive) {
		SystemNotificationList responseList = new SystemNotificationList();

		try {
			Session session = this.getSession();
			VbOrganization organization = getOrganization(session, organizationId);
			VbSalesBook salesBook = getVbSalesBook(session, salesExecutive, organization);
			if (salesBook == null) {
				salesBook = getVbSalesBookNoFlag(session, salesExecutive, organization);
			}
			List<VbSystemNotifications> systemNotifications = session.createCriteria(VbSystemNotifications.class)
					.add(Restrictions.eq("vbSalesBook", salesBook))
					.add(Restrictions.eq("vbOrganization", organization))
					.add(Restrictions.eq("flag", new Integer(1)))
					.list();
			session.close();
			responseList.setNoticationList(new ArrayList<SystemNotification>());
			if (!(systemNotifications.isEmpty())) {
				SystemNotification response = null;
				for (VbSystemNotifications vbSystemNotifications : systemNotifications) {
					response = new SystemNotification();
					response.setId(vbSystemNotifications.getId());
					response.setBusinessName(vbSystemNotifications.getBusinessName());
					response.setInvoiceNo(vbSystemNotifications.getInvoiceNo());
					response.setNotificationStatus(vbSystemNotifications.getNotificationStatus());
					response.setNotificationType(vbSystemNotifications.getNotificationType());
					response.setStatusCode(new Integer(200));
					response.setMessage("success");
					responseList.getNoticationList().add(response);
				}
			}

			if (_logger.isDebugEnabled()) {
				_logger.debug("{} notifications have been found.",
						responseList != null ? responseList.getNoticationList()
								.size() : 0);
			}
		} catch (Exception e) {
			_logger.error(e.toString());
		}
		return responseList;
	}

	/**
	 * This method is responsible to update the read status of
	 * {@link VbSystemNotifications}.
	 * 
	 * @param id - {@link Integer}
	 * @return response - {@link Response}
	 */
	public Response updateStatus(Integer id) {
		Response response = null;
		Session session = null;
		Transaction txn = null;
		try {
			session = this.getSession();
			VbSystemNotifications notification = (VbSystemNotifications) session.get(VbSystemNotifications.class, id);
			if (notification != null) {
				txn = session.beginTransaction();
				notification.setFlag(new Integer(0));
				session.update(notification);
				txn.commit();
			}

			response = new Response();
			response.setMessage("success");
			response.setStatusCode(new Integer(200));
			return response;
		} catch (HibernateException exception) {
			if (txn != null) {
				txn.rollback();
			}
			response = new Response();
			response.setMessage("fail");
			response.setStatusCode(new Integer(500));
			return response;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	/**
	 * This method is responsible to modify the password of an existing user.
	 * 
	 * @param request - {@link PasswordChangeRequest}
	 * @return response - {@link Response}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	public Response changePassword(PasswordChangeRequest request)
			throws DataAccessException {
		Session session = null;
		Transaction txn = null;
		Response response = new Response();
		try {
			session = this.getSession();
			VbOrganization organization = getOrganization(session, request.getOrganizationId());
			String oldPassword = request.getOldPassword();
			String newPassword = request.getNewPassword();
			VbLogin login = (VbLogin) session.createCriteria(VbLogin.class)
					.add(Restrictions.eq("username", request.getUserName()))
					.add(Restrictions.eq("password", PasswordEncryption.encryptPassword(oldPassword)))
					.add(Restrictions.eq("vbOrganization", organization))
					.uniqueResult();
			if (login != null) {
				if (oldPassword.equals(newPassword)) {
					throw new DataAccessException("Old Password and New Password should not same.");
				} else {
					txn = session.beginTransaction();
					login.setPassword(PasswordEncryption.encryptPassword(newPassword));
					login.setFirstTime('N');

					if (_logger.isDebugEnabled()) {
						_logger.debug("Updating VbLogin");
					}
					session.update(login);
					txn.commit();
				}
			} else {
				String message = "Invalid username/password.";

				if (_logger.isWarnEnabled()) {
					_logger.warn(message);
				}
				throw new DataAccessException(message);
			}
			response.setMessage("success");
			response.setStatusCode(new Integer(200));

			if (_logger.isErrorEnabled()) {
				_logger.error("Success response: {}", response);
			}
			return response;
		} catch (HibernateException exception) {
			if (txn != null) {
				txn.rollback();
			}
			response.setMessage("fail");
			response.setStatusCode(new Integer(500));

			if (_logger.isErrorEnabled()) {
				_logger.error("Failure response: {}", response);
			}
			return response;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	/**
	 * This method is responsible to read APK file details from tomcat webapps directory.
	 * 
	 * @return apkDetailsList - {@link List}
	 */
	private void setAPKVersionInfo(LoginResponseForCAU loginResponse) {
		String apkpath = getVbooksApkPath();
		if (apkpath != null) {
			try {
				StringBuffer APKDetails = new StringBuffer(apkpath)
						.append(File.separator).append(ApplicationConstants.TOMCAT_WEBAPPS_DIRECTORY)
						.append(File.separator).append(ApplicationConstants.APK_DIRECTORY)
						.append(File.separator).append(ApplicationConstants.PROPERTIES_FILE_NAME);
				if (APKDetails != null) {
					Properties properties = new Properties();
					properties.load(new FileInputStream(new File(APKDetails.toString())));
					loginResponse.setForceupdate(Boolean.valueOf(properties.getProperty(ApplicationConstants.IS_APK_FORCE_UPDATED)));
					loginResponse.setVersionName(properties.getProperty(ApplicationConstants.APK_VESION_NAME));
				}
			} catch (FileNotFoundException exception) {
				_logger.error(exception.getMessage());
			} catch (IOException exception) {
				_logger.error(exception.getMessage());
			}
		}
	}
	
	
	/**
	 * This method is re responsible to fetch the tomcat home location from the
	 * environmental variable.
	 * 
	 * @return tomcatHome - {@link String}
	 */
	private String getVbooksApkPath() {
		String vbooksApkpath = System.getenv("VBOOKS_APK_PATH");
		if (vbooksApkpath == null) {
			throw new RuntimeException(
					"User Home system/envrionment property is not specified as a JVM arg; use the: [VBOOKS_APK_PATH] to define it. " +
					"E.g., -DVBOOKS_APK_PATH=/home/vekomy/Application/apache-tomcat-7.0.26 or set as environment variable.");
		}
		return vbooksApkpath;
	}
}
