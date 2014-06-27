package com.vekomy.vbooks.organization.dao;

import java.util.ArrayList;
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
import org.springframework.security.authentication.DisabledException;

import com.vekomy.vbooks.exception.DataAccessException;
import com.vekomy.vbooks.hibernate.BaseDao;
import com.vekomy.vbooks.hibernate.model.VbEmployee;
import com.vekomy.vbooks.hibernate.model.VbLogin;
import com.vekomy.vbooks.hibernate.model.VbOrganization;
import com.vekomy.vbooks.hibernate.model.VbOrganizationMapping;
import com.vekomy.vbooks.organization.command.OrganizationCommand;
import com.vekomy.vbooks.organization.command.ValidateUserCommand;
import com.vekomy.vbooks.siteadmin.command.ManageUserResult;
import com.vekomy.vbooks.siteadmin.command.OrganizationResult;
import com.vekomy.vbooks.util.Msg;
import com.vekomy.vbooks.util.Msg.MsgEnum;
import com.vekomy.vbooks.util.OrganizationUtils;

/**
 * @author Satish
 *
 * 
 */
public class OrganizationDao extends BaseDao {
	
	/**
	 * Logger variable holdes _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(OrganizationDao.class);
	
	/**
	 * This method is responsible to get all {@link VbOrganization}s.
	 * 
	 * @return organizationList - {@link List}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public List<VbOrganization> getOrganizations() throws DataAccessException {
		Session session = this.getSession();
		List<VbOrganization> organizationList = session.createCriteria(VbOrganization.class).list();
		session.close();
		
		if(!organizationList.isEmpty()) {
			if(_logger.isDebugEnabled()){
				_logger.debug("{} organizations have been configured in the system.", organizationList.size());
			}
			return organizationList;
		} else {
			String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if(_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}
		
	}
	/**
	 * This method is responsible to get {@link VbOrganization}s based on organization code.
	 * 
	 * @param organization - {@link VbOrganization}
	 * @return organizationList - {@link List}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<VbOrganization> getMyOrganizations(VbOrganization organization) throws DataAccessException {
		ArrayList<VbOrganization> organizationList = new ArrayList<VbOrganization>();
		Session session = this.getSession();
		List<VbOrganization> organizations = session.createCriteria(VbOrganization.class)
				.add(Restrictions.eq("organizationCode", organization.getOrganizationCode()))
				.list();
		session.close();
		
		if (!organizations.isEmpty()) {
			for (VbOrganization vbOrganization : organizations) {
				organizationList.add(vbOrganization);
			}
			
			if(_logger.isDebugEnabled()){
				_logger.debug("organizationList: {}", organizationList);
			}
			return organizationList;
		} else {
			String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if(_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			
			throw new DataAccessException(message);
		}
		
	}
	
	/**
	 * This method is responsible for fetching organization for view based on organization id.
	 * 
	 * @param organizationId - {@link int}
	 * @return organizationInstance - {@link VbOrganization}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	public VbOrganization getOrganization(int organizationId) throws DataAccessException {
		Session session = this.getSession();
		VbOrganization organization = (VbOrganization) session.get(VbOrganization.class, organizationId);
		session.close();
		
		if(organization != null) {
			if(_logger.isDebugEnabled()){
				_logger.debug("organization: {}", organization);
			}
			return organization;
		} else {
			String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if(_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			
			throw new DataAccessException(message);
		}
		
	}
	
	
	/**
	 * This method is responsible for fetching organization for view based on organization id.
	 * 
	 * @param organizationId - {@link int}
	 * @return organizationInstance - {@link VbOrganization}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	public OrganizationResult getOrganizationData(int organizationId) throws DataAccessException {
		Session session = this.getSession();
		OrganizationResult organizationResult = new OrganizationResult();
		VbOrganization organization = (VbOrganization) session.get(VbOrganization.class, organizationId);
		session.close();
		
		if(organization != null) {
			VbOrganization vbOrganization = getMainBranchForOrganization(organization);
			if(vbOrganization != null){
				organizationResult.setMain_branch_name(vbOrganization.getName());
			}
					
			organizationResult.setAddressLine1(organization.getAddressLine1());
			organizationResult.setAddressLine2(organization.getAddressLine2());
			organizationResult.setAlternateMobile(organization.getAlternateMobile());
			organizationResult.setBranchName(organization.getBranchName());
			organizationResult.setCity(organization.getCity());
			organizationResult.setCountry(organization.getCountry());
			organizationResult.setCurrencyFormat(organization.getCurrencyFormat());
			organizationResult.setDescription(organization.getDescription());
			organizationResult.setEmail(organization.getEmail());
			organizationResult.setFullName(organization.getFullName());
			organizationResult.setLandmark(organization.getLandmark());
			organizationResult.setLocality(organization.getLocality());
			organizationResult.setMainBranch(organization.getMainBranch());
			organizationResult.setMobile(organization.getMobile());
			organizationResult.setName(organization.getName());
			organizationResult.setOrganizationCode(organization.getOrganizationCode());
			organizationResult.setUsernamePrefix(organization.getUsernamePrefix());
			organizationResult.setPhone1(organization.getPhone1());
			organizationResult.setPhone2(organization.getPhone2());
			organizationResult.setState(organization.getState());
			organizationResult.setSuperUserName(organization.getSuperUserName());
			organizationResult.setZipcode(organization.getZipcode());
			if(_logger.isDebugEnabled()){
				_logger.debug("organization: {}", organization);
			}
		} else {
			String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if(_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			
			throw new DataAccessException(message);
		}
		return organizationResult;
		
	}
	
	/**This method is responsible for fetching organization codes for all registered organization to avoid duplicate organization code
	 * 
	 * @return organizationCodes - {@link}
	 */
	@SuppressWarnings("unchecked")
	public List<String> getOrganizationCode() {
		Session session = this.getSession();
		ArrayList<VbOrganization> organizationList = (ArrayList<VbOrganization>) session.createCriteria(VbOrganization.class).list();
		ArrayList<String> organizationCodes = new ArrayList<String>();
		for (VbOrganization vbOrganization : organizationList) {
			organizationCodes.add(vbOrganization.getOrganizationCode());
		}
		session.close();
		
		if(_logger.isDebugEnabled()){
			_logger.debug("{} organizationCodes available in the system", organizationCodes.size());
		}
		return organizationCodes;
	}

    /**this method is responsible for valiate organization superusername from vblogin to avoid duplication.
     * 
     * @param validateUserCommand - {@link ValidateUserCommand}
     * @return available - {@link String}
     */
    public String validateUsername(ValidateUserCommand validateUserCommand) {
        String isAvailable = "y";
        Session session = this.getSession();
        VbLogin login = (VbLogin) session.createCriteria(VbLogin.class)
        		.add(Restrictions.eq("username", validateUserCommand.getUsername()))
        		.uniqueResult();
        session.close();
        if(login != null) {
            isAvailable = "n";
        }
		
        return isAvailable;
    }
    
	/**
	 * This method is responsible to Disable {@link VbOrganization},
	 * {@link VbLogin} and {@link VbEmployee} from DB.
	 * 
	 * @param mainBranch - {@link String}
	 * @param organizationCommand - {@link OrganizationCommand}
	 * @param organizationStatus - {@link String}
	 * @throws DataAccessException - {@link DataAccessException}
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void modifyOrganizationStatus(String mainBranch,
			OrganizationCommand organizationCommand, String organizationStatus) throws DataAccessException {
		Session session = this.getSession();
		VbOrganization organization = (VbOrganization) session.get(VbOrganization.class, organizationCommand.getId());
		if (organization != null) {
			Transaction tx = null;
			try {
				tx = session.beginTransaction();
				// if disabled org is Main Branch then disable all sub-branches from organization mapping
				/*if ("Y".equalsIgnoreCase(organization.getMainBranch())) {*/
					if(OrganizationUtils.LOGIN_ENABLED_STRING.equalsIgnoreCase(organizationStatus)){
						String parent=(String)session.createCriteria(VbOrganizationMapping.class)
								             .setProjection(Projections.property("parent"))
								             .add(Restrictions.eq("vbOrganizationBySubBranchId", organization))
								             .uniqueResult();
						if(parent != null){
							String[] orgIdArray=parent.split(",");
							for(int i=0;i<orgIdArray.length;i++){
								Integer idOrg=Integer.parseInt(orgIdArray[i]);
								VbLogin login =(VbLogin) session.createCriteria(VbLogin.class)
										.createAlias("vbOrganization", "organization")
				                        .add(Restrictions.eq("organization.id",idOrg))
				                        .add(Restrictions.eq("enabled",OrganizationUtils.LOGIN_DISABLED))
				                        .uniqueResult();
								if(login != null){
									
									String message = Msg.get(MsgEnum.ORGANIZATION_SUB_BRANCH_ENABLED_ERROR);
									throw new DisabledException(message);
								}
							}
						}
						enableOrDisableCredentials(session, organization.getId(), organization.getSuperUserName(), organizationStatus);
					}else{
						String org=organization.getId().toString();
						Query query=session.createQuery("select vb.vbOrganizationBySubBranchId from VbOrganizationMapping vb where vb.parent like '%,"+org+"' or vb.parent like '"+org+"' or vb.parent like '"+org+",%' or vb.parent like '%,"+org+",%'");
						List<VbOrganization> orgMapList=query.list();
						/*List<VbOrganization> orgMapList=session.createCriteria(VbOrganizationMapping.class)
								                                      .setProjection(Projections.property("vbOrganizationBySubBranchId"))
						                                              .add(Restrictions.ilike("parent",organization.getId().toString(),MatchMode.EXACT))
						                                              .list();*/
						if(! orgMapList.isEmpty()){
							for(VbOrganization subOrgs:orgMapList){
								enableOrDisableCredentials(session, subOrgs.getId(),subOrgs.getSuperUserName(), organizationStatus);
							}
							
						}
						enableOrDisableCredentials(session, organization.getId(), organization.getSuperUserName(), organizationStatus);
					}
				tx.commit();
			} catch (HibernateException exception) {
				if (tx != null) {
					tx.rollback();
				}
				String message = Msg.get(MsgEnum.UPDATE_FAILURE_MESSAGE);
				if(_logger.isErrorEnabled()) {
					_logger.error(message);
				}
				throw new DataAccessException(message);
			}
			catch (DisabledException disabledException) {
				if (tx != null) {
					tx.rollback();
				}
				if(_logger.isErrorEnabled()) {
					_logger.error(disabledException.getMessage());
				}
				throw new DataAccessException(disabledException.getMessage());
			}
			finally {
				if(session != null) {
					session.close();
				}
			}
		} else {
			if(session != null) {
				session.close();
			}
			String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if(_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}
	}
	
	/**
	 * This method is responsible to enable or disable login credentials of user.
	 * 
	 * @param session - {@link Session}
	 * @param organization - {@link VbOrganization}
 	 * @param userName - {@link String}
	 * @param status - {@link String}
	 */
	private void enableOrDisableCredentials(Session session, Integer id,String userName, String status) {
		VbLogin vbLogin = (VbLogin) session.createCriteria(VbLogin.class)
				.createAlias("vbOrganization", "organization")
				.add(Restrictions.eq("username", userName))
				.add(Restrictions.eq("organization.id", id))
				.uniqueResult();
		if (vbLogin != null) {
			if (OrganizationUtils.LOGIN_ENABLED_STRING.equalsIgnoreCase(status)) {
				vbLogin.setEnabled(OrganizationUtils.LOGIN_ENABLED);
			} else {
				vbLogin.setEnabled(OrganizationUtils.LOGIN_DISABLED);
			}
			session.update(vbLogin);
		}
	}
    
    @SuppressWarnings("unchecked")
   	public String validateOrganizationCode(OrganizationCommand organizationCommand) {
   		String isAvailable = "y";
   		Session session = this.getSession();
   		List<VbOrganization> organizationList = session.createCriteria(VbOrganization.class)
   				.add(Expression.eq("organizationCode",organizationCommand.getOrganizationCode()))
   				.add(Expression.ne("id", organizationCommand.getId()))
   				.list();
   		if (!organizationList.isEmpty()) {
   			isAvailable = "n";
   		}
   		session.close();
   		return isAvailable;
   	}
    
    /**
     * This method is responsible to get all the main branches from {@link VbOrganization}.
     * 
     * @return mainBranches - {@link List}
     * 
     */
    @SuppressWarnings("unchecked")
	public List<String> getMainBranches(String mainBranchName,OrganizationCommand organizationCommand){
    	Session session = this.getSession();
    	List<String> mainBranches = session.createCriteria(VbOrganization.class)
    			.setProjection(Projections.property("name"))
    			.add(Expression.like("name", mainBranchName, MatchMode.START))
    			.add(Expression.ne("name", "User Group"))
    			.add(Expression.ne("id", organizationCommand.getId()))
    			.addOrder(Order.asc("name"))
    			.list();
    	session.close();
    	
    	return mainBranches;
    }
    
    /**
     * This Method Is Responsible To Apply Search Criteria On All The Records
     * 
     * @param organizationCommand
     * @return organizationList - {@link List}
     * @throws DataAccessException - {@link DataAccessException}
     */
    @SuppressWarnings("unchecked")
	public List<VbOrganization> searchOrganization(OrganizationCommand organizationCommand) throws DataAccessException {
    	Session session = this.getSession();
    	ArrayList<VbOrganization> organizationList = new ArrayList<VbOrganization>();
		Criteria criteria = session.createCriteria(VbOrganization.class);
		if(organizationCommand != null){
			String country = organizationCommand.getCountry();
			String mainBranch = organizationCommand.getMainBranch();
			if (!StringUtils.isEmpty(country)) {
				criteria.add(Restrictions.like("country", country, MatchMode.START).ignoreCase());
			}
			if (!StringUtils.isEmpty(mainBranch)) {
				if (!"All".equalsIgnoreCase(mainBranch)) {
					criteria.add(Restrictions.like("mainBranch", mainBranch, MatchMode.START).ignoreCase());
				}
			}
		}
		criteria.add(Restrictions.ne("name", "User Group"));
		List<VbOrganization> organizationDetails = criteria.list();
		session.close();
		
		if(!organizationDetails.isEmpty()) {
			for (VbOrganization vbOrganization : organizationDetails) {
				organizationList.add(vbOrganization);
			}
			
			if (_logger.isDebugEnabled()) {
				_logger.debug("{} organizations have been found.", organizationList.size());
			}
			return organizationList;
		} else {
			String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if(_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}
    }
    /**
     * This method is responsible to get all the disabled organization from {@link VbLogin}.
     * 
     * @return disabledOrganization - {@link List}
     * @throws DataAccessException - {@link DataAccessException}
     * 
     */
    @SuppressWarnings("unchecked")
	public List<ManageUserResult> getDisabledOrganizationList() throws DataAccessException{
    	Session session = this.getSession();
    	List<VbLogin> vbLogins = session.createCriteria(VbLogin.class)
    			.add(Expression.eq("enabled", OrganizationUtils.LOGIN_DISABLED))
    			.list();
    	session.close();
    	
    	if(vbLogins != null && !vbLogins.isEmpty()) {
    		List<ManageUserResult> disabledOrganization=new ArrayList<ManageUserResult>();
    		//disabled organization list iteration with all employee under that organization
    		for(VbLogin vbLogin : vbLogins){
    			String oragnizationName=vbLogin.getUsername();
    			ManageUserResult loginResults=new ManageUserResult();
    			loginResults.setDisabledLoginUserName(oragnizationName);
    			disabledOrganization.add(loginResults);
    		}
    		
    		return disabledOrganization;
    	} else {
    		String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if(_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			throw new DataAccessException(message);
    	}
    }
    
	/**
	 * This method is responsible to get main branch name of an organization
	 * {@link VbOrganizationMapping,VbOrganization}.
	 * 
	 * @return mainBranchName - {@link VbOrganization}
	 * @throws DataAccessException - {@link DataAccessException}
	 * 
	 */
	public VbOrganization getMainBranchForOrganization(VbOrganization vbOrgnization) throws DataAccessException {
		Session session = this.getSession();
		VbOrganizationMapping organizationMapping = (VbOrganizationMapping) session.createCriteria(VbOrganizationMapping.class)
				.add(Restrictions.eq("vbOrganizationBySubBranchId", vbOrgnization))
				.uniqueResult();
		
		if (organizationMapping != null) {
			VbOrganization mainBranchName = null;
			VbOrganization mainBranch = organizationMapping.getVbOrganizationByMainBranchId();
			if(mainBranch != null){
				mainBranchName = (VbOrganization) session.get(VbOrganization.class, mainBranch.getId());
			}
			session.close();
			return mainBranchName;
		} else {
			session.close();
			String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if(_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}
	}
   
   /**
	 * This Method is Responsible to Check whether The Entered Organization supper username is
	 * Duplicate Or Not
	 * 
	 * @param username - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return isAvailable - {@link String}
	 */
	public String validateOrgSuperUserName(String username, VbOrganization organization) {
		String isAvailable = "y";
		VbOrganization organizationUserName=null;
		Session session = this.getSession();
		 organizationUserName =(VbOrganization)session.createCriteria(VbOrganization.class)
				.add(Expression.eq("superUserName", username))
				.uniqueResult();
				
		if(organizationUserName == null){
			isAvailable = "n";
		}
		session.close();
		return isAvailable;
	}
	
	/**
	 * This method is responsible for fetching organization for view based on organization id.
	 * 
	 * @param organizationId - {@link int}
	 * @return organizationInstance - {@link VbOrganization}
	 * @throws DataAccessException - {@link DataAccessException}
	 *//*
	@SuppressWarnings("unchecked")
	public OrganizationResult getOrganizationViewResult(int organizationId) throws DataAccessException {
		Session session = this.getSession();
		OrganizationResult orgResult=null;
		VbOrganization organizationInstance = (VbOrganization) session.get(VbOrganization.class, organizationId);
		if(organizationInstance != null) {
			 Populating organization instance data to result class along with if organization is 
			sub-branch fetch main-org_name from vb_mapping table.
			orgResult=new OrganizationResult();
			orgResult.setOrganizationCode(organizationInstance.getOrganizationCode());
			orgResult.setName(organizationInstance.getName());
			orgResult.setMainBranch(organizationInstance.getMainBranch());
			orgResult.setDescription(organizationInstance.getDescription());
			orgResult.setFullName(organizationInstance.getFullName());
			orgResult.setAddressLine1(organizationInstance.getAddressLine1());
			orgResult.setAddressLine2(organizationInstance.getAddressLine2());
			orgResult.setLandmark(organizationInstance.getLandmark());
			orgResult.setLocality(organizationInstance.getLocality());
			orgResult.setCity(organizationInstance.getCity());
			orgResult.setState(organizationInstance.getState());
			orgResult.setCountry(organizationInstance.getCountry());
			orgResult.setZipcode(organizationInstance.getZipcode());
			orgResult.setCurrencyFormat(organizationInstance.getCurrencyFormat());
			orgResult.setPhone1(organizationInstance.getPhone1());
			orgResult.setPhone2(organizationInstance.getPhone2());
			orgResult.setMobile(organizationInstance.getMobile());
			orgResult.setAlternateMobile(organizationInstance.getAlternateMobile());
			orgResult.setEmail(organizationInstance.getEmail());
			//fetching main_branch_name for subBranchId from organizationMapping
			if ("N".equalsIgnoreCase(organizationInstance.getMainBranch())) {
				List<VbOrganizationMapping> subBranchIdList = session.createCriteria(VbOrganizationMapping.class)
						.createAlias("vbOrganizationBySubBranchId", "organization")
						.add(Restrictions.eq("organization.id", organizationInstance.getId()))
						.list();
				if (!subBranchIdList.isEmpty()) {
					for (VbOrganizationMapping subBranchId : subBranchIdList) {
						if (subBranchId.getVbOrganizationBySubBranchId().getId().equals(organizationInstance.getId())) {
							VbOrganization organization = (VbOrganization) session.get(VbOrganization.class, subBranchId.getVbOrganizationByMainBranchId().getId());
							orgResult.setMain_branch_name(organization.getName());
						}

					}
				}
			} 
			session.close();
			
			if(_logger.isDebugEnabled()){
				_logger.debug("organization: {}", organizationInstance);
			}
			return orgResult;
		} else {
			session.close();
			String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if(_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}
		
	}*/
	/**
	 * This Method is Responsible To Check Whether The Given UserName Prefix Is Already Exists or not
	 * 
	 * @param userPrefix
	 * @param organization
	 * @return
	 */
	public Boolean checkUeserPrefixAvailabilty(String userPrefix,
			VbOrganization organization) {
		Boolean isAvailable = Boolean.TRUE;
		Session session = this.getSession();
		VbOrganization vbOrganization =(VbOrganization) session.createCriteria(VbOrganization.class)
				.add(Restrictions.eq("usernamePrefix", userPrefix))
				.uniqueResult();
		session.close();
		if(vbOrganization != null){
			return isAvailable;
		}else{
			isAvailable = Boolean.FALSE;
		}
		return isAvailable;
	}
}
