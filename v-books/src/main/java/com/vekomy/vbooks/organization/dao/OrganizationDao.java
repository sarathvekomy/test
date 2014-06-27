package com.vekomy.vbooks.organization.dao;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;

import com.vekomy.vbooks.hibernate.BaseDao;
import com.vekomy.vbooks.hibernate.model.VbEmployee;
import com.vekomy.vbooks.hibernate.model.VbLogin;
import com.vekomy.vbooks.hibernate.model.VbOrganization;
import com.vekomy.vbooks.organization.command.OrganizationCommand;
import com.vekomy.vbooks.organization.command.ValidateUserCommand;
import com.vekomy.vbooks.siteadmin.command.ManageUserResult;
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
	
	@SuppressWarnings("unchecked")
	public List<VbOrganization> getOrganizations() {
		Session session = this.getSession();
		List<VbOrganization> organizationList = session.createCriteria(VbOrganization.class).list();
		session.close();
		
		if(_logger.isDebugEnabled()){
			_logger.debug("{} organizations have been configured in the system.", organizationList.size());
		}
		return organizationList;
	}
	@SuppressWarnings("unchecked")
	public ArrayList<VbOrganization> getMyOrganizations(VbOrganization organization) {
		ArrayList<VbOrganization> organizationList = new ArrayList<VbOrganization>();
		Session session = this.getSession();
		List<VbOrganization> organizations = session.createCriteria(VbOrganization.class)
				.add(Restrictions.eq("organizationCode", organization.getOrganizationCode()))
				.list();
		for (VbOrganization vbOrganization : organizations) {
			organizationList.add(vbOrganization);
		}
		session.close();
		
		if(_logger.isDebugEnabled()){
			_logger.debug("organizationList: {}", organizationList);
		}
		return organizationList;
	}
	
	public VbOrganization getOrganization(int organizationId) {
		Session session = this.getSession();
		VbOrganization organizationInstance = (VbOrganization) session.get(VbOrganization.class, organizationId);
		session.close();
		if(_logger.isDebugEnabled()){
			_logger.debug("organization: {}", organizationInstance);
		}
		return organizationInstance;
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getOrganizationCode() throws DataAccessException, java.sql.SQLException, ParseException {
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

    public String validateUsername(ValidateUserCommand validateUserCommand) {
        String available = "y";
        Session session = this.getSession();
        VbLogin login = (VbLogin) session.createCriteria(VbLogin.class)
        		.add(Restrictions.eq("username", validateUserCommand.getUsername()))
        		.uniqueResult();
        if(login != null) {
            available = "n";
        }
		session.close();
        return available;
    }
    
    /**
     * This method is responsible to Disable {@link VbOrganization}, {@link VbLogin} and
     * {@link VbEmployee} from DB.
     * 
     * @param organizationCommand - {@link OrganizationCommand}
     * @param organizationStatus - {@link String}
     * 
     */
    @SuppressWarnings("unchecked")
	public void modifyOrganizationStatus(OrganizationCommand organizationCommand,String organizationStatus) {
    	Session session = this.getSession();
		Transaction tx = session.beginTransaction();
		Integer id = organizationCommand.getId();
		VbOrganization organization = (VbOrganization) session.get(VbOrganization.class, id);
		if(organization != null) {
			VbLogin login = (VbLogin) session.createCriteria(VbLogin.class)
					.add(Expression.eq("username", organization.getSuperUserName()))
					.uniqueResult();
			if(login != null) {
				if("Enabled".equals(organizationStatus)){
					login.setEnabled(OrganizationUtils.LOGIN_ENABLED);
				}else{ 
					if("Disabled".equals(organizationStatus)){
						login.setEnabled(OrganizationUtils.LOGIN_DISABLED);
				    }
				}
				session.update(login);
				if(_logger.isDebugEnabled()) {
					_logger.debug("Modifying login credentials of organization: {}", organization.getName());
				}
			}
		} else {
			if(_logger.isErrorEnabled()){
				_logger.error("Organization not found with id: {}", id);
			}
		}
		
		List<VbEmployee> modifiedEmployeeList=new ArrayList<VbEmployee>();
		 modifiedEmployeeList=(List<VbEmployee>)session.createCriteria(VbEmployee.class)
				.createAlias("vbOrganization", "organization")
				.add(Expression.eq("organization.id", id)).list();
			if(modifiedEmployeeList != null){
				for(VbEmployee vbEmployee : modifiedEmployeeList){
			VbLogin login = (VbLogin) session.createCriteria(VbLogin.class)
					.add(Expression.eq("username", vbEmployee.getUsername()))
					.uniqueResult();
			if(login != null) {
				if("Enabled".equals(organizationStatus)){
					login.setEnabled(OrganizationUtils.LOGIN_ENABLED);
				}else{ 
					if("Disabled".equals(organizationStatus)){
						login.setEnabled(OrganizationUtils.LOGIN_DISABLED);
				    }
				}
				session.update(login);
				if(_logger.isDebugEnabled()) {
					_logger.debug("Modifying login credentials of organization: {}", organization.getName());
				}
			}
		  }
		} else {
			if(_logger.isErrorEnabled()){
				_logger.error("Employee not found with organization Id: {}", id);
			}
		}
		
		tx.commit();
		session.close();
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
    	return mainBranches;
    }
    
    /**
     * This Method Is Responsible To Apply Search Criteria On All The Records
     * 
     * @param organizationCommand
     * @return organizationList - {@link List}
     */
    @SuppressWarnings("unchecked")
	public List<VbOrganization> searchOrganization(OrganizationCommand organizationCommand) {
    	if (_logger.isDebugEnabled()) {
			_logger.debug("OrganizationCommand: {}", organizationCommand);
		}
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
				if (mainBranch.equals("All")) {
				} else {
					criteria.add(Restrictions.like("mainBranch", mainBranch, MatchMode.START).ignoreCase());
				}

			}
		}
		criteria.add(Restrictions.ne("name", "User Group"));
		List<VbOrganization> organizationDetails = criteria.list();
		for (VbOrganization vbOrganization : organizationDetails) {
			organizationList.add(vbOrganization);
		}
		if (_logger.isDebugEnabled()) {
			_logger.debug("organizationList: {}", organizationList);
		}
		session.close();
		return organizationList;
    }
    /**
     * This method is responsible to get all the disabled organization from {@link VbLogin}.
     * 
     * @return disabledOrganization - {@link List}
     * 
     */
    @SuppressWarnings("unchecked")
	public List<ManageUserResult> getDisabledOrganizationList(){
    	Session session = this.getSession();
    	List<ManageUserResult> disabledOrganization=new ArrayList<ManageUserResult>();
    	List<VbLogin> vbLogins=new ArrayList<VbLogin>();
    	vbLogins = session.createCriteria(VbLogin.class)
    			.add(Expression.eq("enabled", OrganizationUtils.LOGIN_DISABLED))
    			.list();
    	if(vbLogins != null){
    		for(VbLogin vbLogin : vbLogins){
    			String oragnizationName=vbLogin.getUsername();
    			ManageUserResult loginResults=new ManageUserResult();
    			loginResults.setDisabledLoginUserName(oragnizationName);
    			disabledOrganization.add(loginResults);
    		}
    	}else{
    		if (_logger.isDebugEnabled()) {
    			_logger.debug("No Disabled Oraganization Exists: {}");
    		}
    	}
    	return disabledOrganization;
    }
    
    
}
